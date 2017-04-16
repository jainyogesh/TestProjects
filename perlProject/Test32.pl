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

    $log->debug("YJOPEN1");
    
   #Doing a autoflush of all Write Handlers
    TO_CHLD_W->autoflush(1);
    FR_CHLD_W->autoflush(1);
    FR_CHLD_ERR_W->autoflush(1);
    $log->debug("YJOPEN2");
    my $pid = open3('>&TO_CHLD_R', '<&FR_CHLD_W', '<&FR_CHLD_ERR_W', @_);
    #my $thr = threads->new(\&open3,'>&TO_CHLD_R', '<&FR_CHLD_W', '<&FR_CHLD_ERR_W', @_)->detach;
     $log->debug("YJOPEN3");
    #Closing all the handlers which are passed to Child process so that they are not left hanging in case of race condition
    close(TO_CHLD_R);
    close(FR_CHLD_W);
    close(FR_CHLD_ERR_W);
    
    return (*TO_CHLD_W, *FR_CHLD_R, *FR_CHLD_ERR_R );
}

sub read_in_stdin{
  my $outPipe = shift;
  # We cannot use log here cause it causes contention in log file
  #print("YJ5:$outPipe\n");
  while(<STDIN>){
    my $input =  $_;
    print $outPipe $input or die "error in writing $!";
  }
}

$log->debug("YJ1");


# Listen to what the child says as well as STDIN
my $stdin;
my $parent_in_sel = IO::Select->new();


my $mcas_cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -DJVM_COMPATIBILITY_CHECK_DISABLED=false -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.manage.util.MgmtCommandRunner DD-JNJY7BS 11301 admin';
my $password = 'admin123';
my ($to_chld, $fr_chld, $fr_chld_err) = _open3($mcas_cmd);
my $parent_out_sel = IO::Select->new();
$parent_in_sel->add($fr_chld,$fr_chld_err);

if ($^O =~ /Win32/) {
  $log->debug("YJ2");
  local (*STDIN_R,     *STDIN_W);
  _pipe(*STDIN_R,     *STDIN_W    ) or die $^E;
  $log->debug("YJ3");
  STDIN_W->autoflush(1);
  my $thr = threads->new(\&read_in_stdin,\*STDIN_W)->detach;
  $log->debug("YJ6");
  $stdin = *STDIN_R;
}else{
 $stdin = \*STDIN;
}

$parent_in_sel->add($stdin);

$log->debug("YJ7");
my $stdout_removed = 0;
my $stderr_removed = 0;
while(my @ready =  $parent_in_sel->can_read) {
  $log->debug("YJ8");
  #my ($ins, $outs) = IO::Select::select($parent_in_sel, $parent_out_sel, undef);
  #for my $fh (@$ins) {
  #  $log->debug("YJ10");
  #    my $stdin_sel = IO::Select->new($fh);
  #    $log->debug("YJ12");
  #    ## Read byte by byte:
  #    my $stdin_buffer;
  #    my $offset = 0;
  #    while( $stdin_sel->can_read(0.1) ) {
  #      $log->debug("YJ12.1");
  #      sysread($fh, $stdin_buffer , 1, $offset);
  #      $offset = length($stdin_buffer) if defined($stdin_buffer);
  #      $log->debug("YJ12.2:offset:$offset");
  #      $log->debug("YJ13:$stdin_buffer")if defined ($stdin_buffer);
  #    }
  #    print STDOUT "GOTFROMIN:$stdin_buffer:Exactly\n";
  #    #$parent_in_sel->remove($fh) if eof($fh);
  #}
  #$log->debug("YJ14");
  #for my $fh (@$outs) {
  #  $log->debug("YJ15");
  #  my $io = new IO::Handle;
  #    my @lines;
  #    if ($io->fdopen(fileno($fh),"r")) {
  #      $log->debug("YJ16");
  #      $io->blocking(0);
  #      $log->debug("YJ16.1");
  #      @lines = $io->getlines();
  #      $log->debug("YJ16.2");
  #      $io->flush;
  #      $log->debug("YJ16.3");
  #      $io->close;
  #    }
  #    $log->debug("YJ17");
  #    foreach my $child_line (@lines) {
  #      print STDOUT "STDOUT:$child_line\n";
  #    }
  #    $log->debug("YJ18");
  #    $parent_out_sel->remove($fh) if eof($fh);
  #}
  foreach my $fh (@ready) {
    $log->debug("YJ9");
    
    if (fileno($fh) == fileno($fr_chld) || fileno($fh) == fileno($fr_chld_err)){
      #my $in_sel = IO::Select->new();
      ##$in_sel->add($fh);
      #if (fileno($fh) == fileno($fr_chld)){
      #  $log->debug("YJ24");
      #  $in_sel->add($fr_chld);
      #}else{
      #  $log->debug("YJ25");
      #  $in_sel->add($fr_chld_err);
      #}
      #my $output;
      #
      #while ($in_sel->can_read) {
      #  $log->debug("YJ26");
      #   foreach my $fhin ($in_sel->can_read) {
      #    $log->debug("YJ27");
      #    my $buf;
      #    my $bufSize = 5; # We are just putting some size which seems to be reasonable
      #    my $bytes_read = sysread($fhin, $buf,$bufSize);
      #    $output .= $buf;
      #    $log->debug($output);
      #    #$in_sel->remove($fhin) if (!$bytes_read);
      #    $parent_sel->remove($fh) if (!$bytes_read);
      #   }
      #}
      
      
      #$log->debug("YJ14");
      #my $io = new IO::Handle;
      #$log->debug("YJ15");
      #my @lines;
      #$log->debug("YJ16");
      #if ($io->fdopen(fileno($fh),"r")) {
      #  $log->debug("YJ17");
      #  $io->blocking(0);
      #  $log->debug("YJ18");
      #  @lines = $io->getlines();
      #  $log->debug("YJ19");
      #  $io->flush;
      #  $log->debug("YJ20");
      #  $io->close;
      #}
      #
      #foreach my $child_line (@lines) {
      #  print STDOUT "STDOUT:$child_line\n";
      #}
      #
      #$parent_in_sel->remove($fh) if eof($fh);
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
              $parent_in_sel->remove($fh) ;
              $stdout_removed = 1;
             }else{
              $log->debug("YJ18.2");
              $parent_in_sel->remove($fh) ;
              $stderr_removed = 1;
             }
            if ($stdout_removed && $stderr_removed) {
              exit;
            }
            
          }
          $log->debug("YJ19");
          next;
     # push(@lines, $stdout);
    
    
    
    

    #$log->debug("YJ14");
    #if(not defined $lines[0]) {
    #  $log->debug("YJ15");
    #  # There was an error reading from this fh
    #  last;
    #}
    #elsif (length($lines[0]) == 0) {
    #  $log->debug("YJ16");
    #  $parent_sel->remove($fh);
    #  next;
    #}
    #else {
    #  $log->debug("YJ17");
    #  #if (fileno($fh) == fileno($stdin)) {
    #  #  # This is keyboard input which should be sent to the CHILD
    #  #  foreach my $child_line (@lines) {
    #  #    print STDOUT "GOTFROMIN:$child_line:Exactly\n";
    #  #  }
    #  #}else{
    #    if (fileno($fh) == fileno($fr_chld)) {
    #      $log->debug("YJ19");
    #      foreach my $child_line (@lines) {
    #        print STDOUT "STDOUT:$child_line\n";
    #      }
    #    }else{
    #      $log->debug("YJ20");
    #      foreach my $child_line (@lines) {
    #        print STDOUT "STDERR:$child_line\n";
    #      }
    #    }
    #  #}
    #}
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
      #print STDOUT "GOTFROMIN:$stdin_buffer:Exactly\n";
      print $to_chld $stdin_buffer;
      $log->debug("YJ13.1");
      next;
    }
  }
}