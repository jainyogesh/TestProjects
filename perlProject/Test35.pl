#!/usr/bin/perl -w
use strict;
use warnings;
use threads;
use threads::shared;
use IO::Select qw( );
use IPC::Open3 qw( open3 );
use Socket     qw( AF_UNIX SOCK_STREAM PF_UNSPEC );
use Symbol;
use IO::Handle;
use POSIX qw( strftime WNOHANG );


use lib ('D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/lib/perl');
use Distra::Common;
use Distra::File::Log;
$|++;
my $base_directory = "D:/YJ/temp/";

#
# Init
#

# Setup our logging
LOGGING: {
  my $todays_date = strftime("%Y%m%d", localtime(time));
  my $log_base_directory = $base_directory;
  $log_base_directory =~ s/\/install\/([^\/]+)$//;

  my $log_file = $log_base_directory .$todays_date . ".txt";
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

sub read_in_stdin{
  # We cannot use log here cause it causes contention in log file
  my $outPipe = shift;
  while(<STDIN>){
    my $input =  $_;
    print $outPipe $input or die "error in writing $!";
  }
}

$log->debug("YJ1");


# Listen to what the child says as well as STDIN
my $stdin;
my $select = IO::Select->new();


my $mcas_cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -DJVM_COMPATIBILITY_CHECK_DISABLED=false -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.manage.util.MgmtCommandRunner DD-JNJY7BS 11301 admin';
my $password = 'admin123';
my ($pid,$to_chld, $fr_chld, $fr_chld_err) = pipe_open3($mcas_cmd);

$select->add($fr_chld,$fr_chld_err);

if ($^O =~ /Win32/) {
  $log->debug("YJ2");
  local (*STDIN_R,     *STDIN_W);
  socketpair_pipe(*STDIN_R,     *STDIN_W    ) or die $^E;
  $log->debug("YJ3");
  STDIN_W->autoflush(1);
  my $thr = threads->new(\&read_in_stdin,\*STDIN_W)->detach;
  $log->debug("YJ6");
  $stdin = *STDIN_R;
}else{
 $stdin = \*STDIN;
}

$select->add($stdin);

$log->debug("YJ7");
my $stdout_removed = 0;
my $stderr_removed = 0;
while(my @ready =  $select->can_read) {
  $log->debug("YJ8");
  foreach my $fh (@ready) {
    $log->debug("YJ9");
    
    if (fileno($fh) == fileno($fr_chld) || fileno($fh) == fileno($fr_chld_err)){
            $log->debug("YJ14");
      my $buf;
          my $bufSize = 4096*64; # We are just putting some size which seems to be reasonable
          $log->debug("YJ15");
          my $bytes_read = sysread($fh, $buf,$bufSize);
          $log->debug("YJ16");
          
          if ($buf =~ /^Password:/) {
                  print $to_chld $password . "\n";
                  $log->debug("YJ17");
          }else{
            print STDOUT $buf if defined ($buf) && length($buf) > 0;
            $log->debug("YJ17.1");
          }
          if (!$bytes_read){
            $log->debug("YJ18");
             if (fileno($fh) == fileno($fr_chld)){
              $log->debug("YJ18.1");
              $select->remove($fh) ;
              $stdout_removed = 1;
             }else{
              $log->debug("YJ18.2");
              $select->remove($fh) ;
              $stderr_removed = 1;
             }
            if ($stdout_removed && $stderr_removed) {
              exit;
            }
            
          }
          $log->debug("YJ19");
          next;
     
  }elsif (fileno($fh) == fileno($stdin)) {
      $log->debug("YJ10");
      my $stdin_sel = IO::Select->new($fh);
      $log->debug("YJ12");
      ## Read byte by byte:
      my $stdin_buffer;
      my $offset = 0;
      while( $stdin_sel->can_read(0.1) ) {
        $log->debug("YJ12.1");
        sysread($fh, $stdin_buffer , 1, $offset);
        $offset = length($stdin_buffer) if defined($stdin_buffer);
        $log->debug("YJ12.2:offset:$offset");
        $log->debug("YJ13:$stdin_buffer")if defined ($stdin_buffer);
      }
      print $to_chld $stdin_buffer;
      $log->debug("YJ13.1");
      next;
    }
  }
}