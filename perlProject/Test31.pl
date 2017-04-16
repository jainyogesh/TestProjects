#!/usr/bin/perl -w
use strict;
use lib ('D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/lib/perl');
use Carp qw (croak confess);
use IO::Handle;
use Distra::File::Log;
use POSIX qw ( strftime WNOHANG );
use warnings;

use IO::Select;

use IPC::Open3;
use Socket     qw( AF_UNIX SOCK_STREAM PF_UNSPEC );
my $base_directory = "D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT";

#
# Init
#

# Setup our logging
LOGGING: {
  my $todays_date = strftime("%Y%m%d", localtime(time));
  my $log_base_directory = $base_directory;
  $log_base_directory =~ s/\/install\/([^\/]+)$//;

  my $log_file = $log_base_directory . "/log/operations/log" . $todays_date . ".txt";
  $log = Distra::File::Log->new (
                                  filename => $log_file,
                                  mode => 'append',
                                  maxlevel => '7',
                                  mask_words => '1',
                                );


  $log->neverLogPasswords();
  $log->debug("Started script: $0");
  $log->debug("Script arguments: @ARGV") if (@ARGV);
};





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

    pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $!;
    if ($^O =~ /Win32/) {
        #_pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $^E;
    } else {
        #pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $!;
    }

    $log->debug("YJIN");
    
   #Doing a autoflush of all Write Handlers
    TO_CHLD_W->autoflush(1);
    FR_CHLD_W->autoflush(1);
    FR_CHLD_ERR_W->autoflush(1);
    
    my $pid = open3('>&TO_CHLD_R', '<&FR_CHLD_W', '<&FR_CHLD_ERR_W', @_);
    $log->debug("YJIN1");
    #Closing all the handlers which are passed to Child process so that they are not left hanging in case of race condition
    close(TO_CHLD_R);
    close(FR_CHLD_W);
    close(FR_CHLD_ERR_W);
    
    return ( $pid, *TO_CHLD_W, *FR_CHLD_R, *FR_CHLD_ERR_R );
}

#my $mcas_cmd = ($^O =~ /Win32/)?'java -cp D:\YJ\Temp\mcas_test UserInputTest 3':'java -cp ~/YJ/Temp/mcas_test UserInputTest E';
#my $password = 'PasswordInputIYJYJYJYJYJ';

my $mcas_cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,address=upp1:5008 -Xmx128m -DJVM_COMPATIBILITY_CHECK_DISABLED=false -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.manage.util.MgmtCommandRunner DD-JNJY7BS 11301 admin';
my $password = 'admin1234';

$log->debug("YJ1");
my ($pid, $to_chld, $fr_chld, $fr_chld_err) = _open3($mcas_cmd) or do {print("open:$$ : $!\n"); die "failed";};

print $to_chld "$password\n";
close($to_chld);
$log->debug("YJ2");    

my $stdin = new IO::Handle;
$stdin->fdopen(fileno(STDIN),"r");
my $sel = new IO::Select; # create a select object
$sel->add($fr_chld,$fr_chld_err,$stdin); # and add the fhs
my $stdout;
my $stderr;
while (my @ready = $sel->can_read) {
  $log->debug("YJ3");
  
  foreach my $fh (@ready) {
    #my @lines;
    #$fh->blocking(0);
    
    #my $in_select = new IO::Select;
    #$in_select->add($fh);
    ## Read byte by byte:
    #my $stdin_buffer;
    #my $offset = 0;
    #my $bytes_read;
    #while($in_select->can_read ) {
    #  
    #    $bytes_read = sysread($fh, $stdin_buffer , 4096, $offset);
    #    $offset = length($stdin_buffer) if defined($stdin_buffer);
    #  
    #}
    #$log->debug("BUFFER:".$stdin_buffer);
    #push(@lines, $stdin_buffer);
    #  
    #$log->debug("Lines:".@lines);  
    #if(not defined $lines[0]) {
    #  # There was an error reading from this fh
    #  next;
    #}
    #elsif (length($lines[0]) == 0) {
    #  #$sel->remove($fh);
    #  next;
    #} else {
    #  if ($fh == fileno($fr_chld)) {
    #    # This is from our child process, so print it to the screen
    #    # The only exception is if it is the Password: prompt, then we send back our password
    #    # without prompting the end-user
    #    if ($lines[0] =~ /^Password:/) {
    #      print $to_chld $password . "\n";
    #    }
    #    else {
    #      # This is just a standard line from the child, so we print it for the end-user to see
    #      foreach my $stdout_line (@lines) {
    #        print STDOUT $stdout_line;
    #      }
    #    }
    #  }
    #  elsif ($fh == fileno(\*STDIN)) {
    #    # This is keyboard input which should be sent to the CHILD
    #    foreach my $child_line (@lines) {
    #      print $to_chld $child_line;
    #    }
    #  }
    #}
    
    #my $buf;
    #my $offset = 0;
    #my $in_select = new IO::Select;
    #$in_select->add($fh);
    #my $bytes_read;
    #while ($in_select->can_read) {
    #  $bytes_read = sysread($fh, $buf , 1, $offset);
    #  $offset = length($buf);
    #  #log->debug("BUFFER:".$buf) if defined($buf);
    #  
    #  $in_select->remove($fh) if (!$bytes_read);
    #}
    #if (fileno($fh) == fileno($fr_chld_err)) {$stderr .= $buf;}
    #else                                {$stdout .= $buf;}
    #
    #
    #$sel->remove($fh) if (!$bytes_read);
    #log->debug("BUFFER:".$buf) if defined($buf);
    
    if (fileno($fh) != fileno($stdin)){
      my $buf;
      my $bufSize = 4096; # We are just putting some size which seems to be reasonable
      my $bytes_read = sysread($fh, $buf,$bufSize);
      if (fileno($fh) == fileno($fr_chld_err)) {$stderr .= $buf;}
      elsif  (fileno($fh) == fileno($fr_chld))                              {$stdout .= $buf;} 
      $sel->remove($fh) if (!$bytes_read);
    }else{
      $sel->remove($fh);
    }
    
    
    #$fh->blocking(0);
    #my @lines = $fh->getlines();
    #$fh->flush;
    #
    #$log->debug("LINES:".@lines);
            
    #$sel->remove($fh) if (!$bytes_read);
  }
  
}

$log->debug("STDOUT:".$stdout) if defined($stdout);
$log->debug("STDERR:".$stderr) if defined($stderr);  

waitpid($pid, 0);

#print("STDOUT:\n$stdout");
#print("\n");
#print("STDERR:\n$stderr");