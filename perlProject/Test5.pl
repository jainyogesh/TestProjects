#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );
use IO::Select qw( );
use IPC::Open3 qw( open3 );
use Socket     qw( AF_UNIX SOCK_STREAM PF_UNSPEC );

sub _pipe {
    socketpair($_[0], $_[1], AF_UNIX, SOCK_STREAM, PF_UNSPEC)
        or return undef;
    shutdown($_[0], 1);  # No more writing for reader
    shutdown($_[1], 0);  # No more reading for writer
    return 1;
}

sub _open3 {
    local (*TO_CHLD_R,     *TO_CHLD_W);
    local (*FR_CHLD_R,     *FR_CHLD_W);
    local (*FR_CHLD_ERR_R, *FR_CHLD_ERR_W);

    if ($^O =~ /Win32/) {
        pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $^E;
    } else {
        pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $!;
    }
    print ( @_);
    my $pid = open3('<&TO_CHLD_R', '>&FR_CHLD_W', '>&FR_CHLD_ERR_W', @_);
    
    return ( $pid, *TO_CHLD_W, *FR_CHLD_R, *FR_CHLD_ERR_R );
}

sub exec_cmd_suppress_stdin {
  print("Inside exec_cmd_suppress_stdin\n");
  my ($arg_ref) = @_;

  confess "No command defined" if ( !exists($arg_ref->{'command'}) || !defined($arg_ref->{'command'}) );
  #confess "No stdin value defined" if ( !exists($arg_ref->{'stdin_value'}) || !defined($arg_ref->{'stdin_value'}) );

  my $command = $arg_ref->{'command'};
  my $timeout = $arg_ref->{'timeout'};
  my $stdin_value;
  my $stdout;
  my $stderr;
  my $exit_code;
  my $child_pid;

  my ($infh,$outfh,$errfh);
  #$errfh = gensym();

  # Setup our signal handlers
  local $SIG{CHLD} = sub {
    if (defined($child_pid)) {
      if ( ( waitpid($child_pid, 0) > 0) && ($? > 0) ) {
        if (defined($stderr)) {
          chomp $stderr;
          confess $stderr;
        }
      }
    }
  };

  local $SIG{ALRM} = sub {
    # Time out after $timeout seconds
    confess "Timed out after $timeout seconds executing command: $command";
  };
  print("just before eval\n");
  eval {
    #$child_pid = _open3($infh, $outfh, $errfh, $command);
    ($child_pid, $infh, $outfh, $errfh) = _open3($command);
    
    #print $infh "password\n";
    if (defined($arg_ref->{'stdin_value'})) {
        $stdin_value = $arg_ref->{'stdin_value'};
       syswrite($infh,$stdin_value."\n");
    }
    
    
    # $new_sock->flush;
    close ($infh);
  };
  print("just after eval\n");
  # Setup our alarm
  alarm $timeout if defined($timeout);

  
  my %objs;

  my $in_sel  = IO::Select->new();
  my $out_sel = IO::Select->new();

  for my $fh ($outfh, $errfh) {
      my $obj = {
          buf => '',
      };
      $objs{ fileno($fh) } = $obj;
      $in_sel->add($fh);
  }
  
  

  while ($in_sel->count()+ $out_sel->count()) {
 my ($ins, $outs) = IO::Select::select($in_sel, $out_sel, undef);
 #while ($in_sel->count()) {
 #     my ($ins) = IO::Select::select($in_sel, undef, undef);

      for my $fh (@$ins) {
          my $obj = $objs{ fileno($fh) };
          our $buf; local *buf = \( $obj->{buf} );
          my $bytes_read = sysread($fh, $buf, 64*1024, length($buf));
          if (!$bytes_read) {
              warn("Error reading from child: $!\n")
                  if !defined($bytes_read) && !$!{ECONNRESET};
              $in_sel->remove($fh);
          }
      }

      for my $fh (@$outs) {
      }
    }

  $stdout = $objs{ fileno( $outfh) }{buf};
  $stderr = $objs{ fileno( $errfh ) }{buf};
  close($outfh);
  close($errfh);
  
  #In Windows a Carriage Return is added at the end. Trimming the same.
  $stdout =~ s/\r//g;
  $stderr =~ s/\r//g;
  #my $sel = new IO::Select; # create a select object
  #$sel->add($outfh,$errfh); # and add the fhs
  #print("starting while loop now\n");
  ## $sel->can_read will block until there is data available
  ## on one or more fhs
  #while(my @ready = $sel->can_read) {
  #  # now we have a list of all fhs that we can read from
  #  foreach my $fh (@ready) { # loop through them
  #    my $line;
  #    # read up to 4096 bytes from this fh.
  #    # if there is less than 4096 bytes, we'll only get
  #    # those available bytes and won't block.  If there
  #    # is more than 4096 bytes, we'll only read 4096 and
  #    # wait for the next iteration through the loop to
  #    # read the rest.
  #    my $len = sysread $fh, $line, 4096;
  #    if(not defined $len){
  #      # There was an error reading
  #      confess "Error from child: $!\n";
  #    } elsif ($len == 0){
  #      # Finished reading from this FH because we read
  #      # 0 bytes.  Remove this handle from $sel.
  #      # we will exit the loop once we remove all file
  #      # handles ($outfh and $errfh).
  #      $sel->remove($fh);
  #      next;
  #    } else { # we read data alright
  #      if($fh == $outfh) {
  #        $stdout .= $line;
  #      } elsif($fh == $errfh) {
  #        $stderr .= $line;
  #      }
  #    }
  #  }
  #}
  #print("outside while loop now\n");
  
  # Wait for pid to die
  waitpid $child_pid, 0;

  
  # Disable alarm
  alarm 0 if defined($timeout);

  if ($@) {
    $stderr = $@;
  }

  if (defined($stderr)) {
    chomp $stderr;
    if (length($stderr) > 0) {
      confess "$stderr";
    }
  }

  # If we got this far, all is good
  # Do some cleanup just in case
  if (defined($stdin_value)) {
 
  $stdout =~ s/^$stdin_value// if defined($stdout);
  }
  return $stdout;
}

my $win_cmd = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/java.exe -cp D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.security.util.Obfuscator 2';
my $linux_cmd = 'java -cp /home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar:/home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.security.util.Obfuscator 2';
my $cmd = ($^O =~ /Win32/) ? $win_cmd : $linux_cmd;
my $parameter = 'password';
#my $cmd = 'for /d %I in ("C:\Program Files\Java\jdk1.6.0_43") do @echo|set /p TEMP_LONG_PATH=%~sI';
#my $cmd = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/keytool.exe -genkey -alias foo -keystore D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/keystore -storepass REDACTED -keypass REDACTED -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"';
#my $cmd1 = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/keytool.exe -delete -alias foo -keystore D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/keystore -storepass REDACTED';
for(my $i =0 ; $i <100; $i++){
    my $output = eval {
    exec_cmd_suppress_stdin({
      #command => (($i%2 ==0) ? $cmd : $cmd1),
      command => $cmd,
      stdin_value => $parameter,
    });
  };
  if ($@) {
    confess $@;
  }
  elsif (!defined($output)) {
    confess "Unable to generate obfuscated database";
  }
print($i. "->" .$output);
print("Done");
}
 
