#!/usr/bin/perl -w
use strict;
use base 'Exporter';
use Carp qw(confess croak carp);
use Carp::Heavy;
use IPC::Open3;
use IO::Select;
use IO::Handle;
use POSIX ":sys_wait_h";
use Symbol;
use Socket qw( AF_UNIX SOCK_STREAM PF_UNSPEC );

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
    
    #We do not need to perform a select on this pipe hence leaving it as normal file handle.
    #Also, changing it to socketpair does not help because external java process is
    #not able to read from Std Input in case of Socket
    pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $^E;
    
    if ($^O =~ /Win32/) {
        _pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $^E;
    } else {
        pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $!;
    }
    
    #Doing a autoflush of all Write Handlers
    TO_CHLD_W->autoflush(1);
    FR_CHLD_W->autoflush(1);
    FR_CHLD_ERR_W->autoflush(1);
    
    my $pid = open3('>&TO_CHLD_R', '<&FR_CHLD_W', '<&FR_CHLD_ERR_W', @_);
    
    #Closing all the handlers which are passed to Child process so that they are not left hanging in case of race condition
    close(TO_CHLD_R);
    close(FR_CHLD_W);
    close(FR_CHLD_ERR_W);
    
    return ( $pid, *TO_CHLD_W, *FR_CHLD_R, *FR_CHLD_ERR_R );
}

sub exec_cmd_suppress_stdin {
  print("Inside exec_cmd_suppress_stdin\n");
  my ($arg_ref) = @_;

  confess "No command defined" if ( !exists($arg_ref->{'command'}) || !defined($arg_ref->{'command'}) );
  confess "No stdin value defined" if ( !exists($arg_ref->{'stdin_value'}) || !defined($arg_ref->{'stdin_value'}) );

  my $stdin_value = $arg_ref->{'stdin_value'};
  my $command = $arg_ref->{'command'};
  my $timeout = $arg_ref->{'timeout'};

  my $stdout;
  my $stderr;
  my $exit_code;
  my $child_pid;

  my ($infh,$outfh,$errfh);
  $errfh = gensym();

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
  
    eval {
        ($child_pid, $infh, $outfh, $errfh) = _open3($command);
        syswrite($infh,$stdin_value. "\n");
        close ($infh);
    
        # Setup our alarm
        alarm $timeout if defined($timeout);
    
        my $sel = new IO::Select; # create a select object
        $sel->add($outfh,$errfh); # and add the fhs
    
         while (my @ready = $sel->can_read) {
          foreach my $fh (@ready) {
            if (fileno($fh) == fileno($errfh)) {$stderr .= <$errfh>;}
            else                                {$stdout .= <$outfh>;} 
            $sel->remove($fh) if eof($fh);
          }
      }
     
     # Disable alarm
      alarm 0 if defined($timeout);
      
      close($outfh);
      close($errfh);
      };
  
  # $sel->can_read will block until there is data available
  # on one or more fhs
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
  #      if(fileno($fh) == fileno($outfh)) {
  #        $stdout .= $line;
  #      } elsif(fileno($fh) == fileno($errfh)) {
  #        $stderr .= $line;
  #      }
  #    }
  #  }
  #}
  
  # Wait for pid to die
  waitpid $child_pid, 0;

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
  $stdout =~ s/^$stdin_value// if defined($stdout);
  
  #In Windows a Carriage Return is added at the end. Trimming the same.
  $stdout =~ s/\r//g if defined($stdout);;
  
  return $stdout;
}

my $win_cmd = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/java.exe -cp D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.security.util.Obfuscator 2';
my $linux_cmd = 'java -cp /home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar:/home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.security.util.Obfuscator 2';
my $cmd = ($^O =~ /Win32/) ? $win_cmd : $linux_cmd;
my $parameter = 'password';
#my $cmd = 'for /d %I in ("C:\Program Files\Java\jdk1.6.0_43") do @echo|set /p TEMP_LONG_PATH=%~sI';
#my $cmd = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/keytool.exe -genkey -alias foo -keystore D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/keystore -storepass REDACTED -keypass REDACTED -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"';
#my $cmd1 = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/keytool.exe -delete -alias foo -keystore D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/keystore -storepass REDACTED';
#for(my $i =0 ; $i <100; $i++){
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
print($output);
print("Done");
#}