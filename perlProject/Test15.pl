#!/usr/bin/perl -w
use strict;
use Win32;
use Fcntl qw(:DEFAULT :flock);
use Socket qw( AF_UNIX SOCK_STREAM PF_UNSPEC );

sub my_print{
    print(shift . "\n");
}

# Declare our global variables
use vars qw($USE_SYSLOG
            $NUM_PROCESS_MANAGERS
            $INITIAL_HEARTBEAT_TIME
            $DEFAULT_WAKEUP_TIME
            $DEFAULT_STARTUP_TIME
            $DEFAULT_KILL_TIME
            $MAX_EXIT_TIME
            $SHUTDOWN_EXIT_CODE
            $SYSLOG_FACILITY
            $LOG_DIR
            $LOG_FILE);
            
$DEFAULT_WAKEUP_TIME    = 5;  # If nothing to do, when should we wake up?
$DEFAULT_STARTUP_TIME   = 2;  # Time to wait before deciding a process successfully started up

use Config;
my $pathsep = $Config{path_sep};
my_print "pathsep $pathsep\n";

my $cmd = 'C:\Program Files\Java\jdk1.6.0_43';

my $short_cmd = Win32::GetShortPathName($cmd);

my_print($short_cmd . "\n");
my $PROC_DIR = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch\install\2.5-SNAPSHOT\executive\procmgr\process';
#signalProcessManagers();
#print("Status:".processManagerRunning());
#kill 'HUP', 7284;
my $current_pid = $$;
my_print($current_pid);

if ($^O =~ /Win32/){
    socketpair(READER, WRITER, AF_UNIX, SOCK_STREAM, PF_UNSPEC)
        or return undef;
    shutdown(READER, 1);  # No more writing for reader
    shutdown(WRITER, 0);  # No more reading for writer
}else{
    pipe(READER, WRITER);
}
WRITER->autoflush();

my $NOTIFIED   = "1";
my $CHILD_DIED = "2";
my $TERMINATED = "3";

my $procDir = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch\install\2.5-SNAPSHOT\executive\procmgr\process';
$PROC_DIR = $procDir;
opendir(DIR, $PROC_DIR) or die "Can't open $PROC_DIR: $!\n";
    my @files = grep(/^\.procmgr\.(\d+)$/,readdir(DIR));
    closedir(DIR);
    foreach my $file (@files) {
        my_print($file);
    while (-z "$PROC_DIR/$file") {
        my_print('File is empty');
        open(SIGFILE, "+<$PROC_DIR/$file");
        lockFile(*SIGFILE);
        seek SIGFILE, 0, 0;
        truncate SIGFILE, 0;
        print SIGFILE 'HUP';
        close (SIGFILE);
      }
    }
#if ($^O =~ /Win32/){
#  # Get current process PID
#  my $pid_file = "$procDir/.procmgr.6300";
#  # Fork it and run a seperate thread to read the content from PID File
#  my $pid = fork;
#  if ($pid == 0) {
#    # Child Process (In windows it is just a new thread isntead of separate process)
#    while (1) {
#        my_print('Inside while');
#      if (! -z $pid_file) {
#        open(PIDFILE, "+<",$pid_file);
#        lockFile(*PIDFILE);
#        my_print('Locked File');
#        chomp(my $signal = <PIDFILE>);
#        my_print('Got signal');
#          if ($signal eq "HUP") {
#            syswrite WRITER, $NOTIFIED, 1;
#          } elsif ($signal eq "TERM"){
#            syswrite WRITER, $TERMINATED, 1;
#          }else{
#            #IGNORE
#            my_print 'IGNORED:'.$signal;
#          }
#          
#        seek PIDFILE, 0, 0;
#          truncate PIDFILE, 0;
#        close (PIDFILE);
#        
#        
#        #open(HHH, ">",$pid_file);
#        #lockFile(*HHH);
#        ##
#        #  
#        #  close (HHH);
#      }
#    }
#  }else{
#    my_print("I started the child");
#    continue;
#  }
#    
#}

#my $readFiles = '';
#vec($readFiles, fileno(READER), 1) = 1;
#
#my $signalHandlerMsg = '';
#
#while (not $signalHandlerMsg eq $TERMINATED)
#{
#    my_print("In main while");
#  my @files;
#  my @procMgrPids;
#
#  open(PROC_LOCK, ">>$procDir/.lock") or do { my_print( "Couldn't open $procDir/.lock: $!"); 
#                                         sleep 5; next; };
#  lockFile(*PROC_LOCK)                or do {my_print("flock failed locking $procDir/.lock: $!");
#                                         close(PROC_LOCK); sleep 5; next; };
#
#  opendir(DIR, $procDir) or fatal("Can't open $procDir: $!\n");
#  for (readdir(DIR)) {
#    /^[^\.].*\.proc$/ and push @files, $_;
#  }
#  closedir(DIR);
#
#  # Make sure all procmgrs are still running and clean up any
#  # files for ones that are dead
#  #manageProcessManagers($procDir, $NUM_PROCESS_MANAGERS);
#
#  # Get files in a random order so multiple instances of process managers
#  # will hopefully suffer less contention for file locks
#  my @randFiles = map { $_->[0] } sort { $a->[1] <=> $b->[1] } map { [$_, rand] } @files;
#
#  # Keep track of when we should next wake up
#  my $firstWakeupTime;
#  #for (@randFiles) {
#  #  my $waketime = processFile("$procDir/$_", *PROC_LOCK);
#  #  $firstWakeupTime = $waketime if
#  #    $waketime and (not defined $firstWakeupTime or $waketime < $firstWakeupTime);
#  #}
#
#  my $wakeupTime = ($firstWakeupTime ? $firstWakeupTime : 1 + rand($DEFAULT_WAKEUP_TIME - 1));
#
#  # Clean up any .exit files that were written over 10 seconds ago
#  time - (stat($_))[9] > 10 and unlink $_ for <$procDir/*.exit>;
#
#  # Release our lock
#  close(PROC_LOCK);
#  my_print("after closeproclock");
#  if ($^O =~ /Win32/){
#    if (! -z "$procDir/.procmgr.6300") {
#    open(PIDFILE, "+<$procDir/.procmgr.6300");
#        lockFile(*PIDFILE);
#        my_print('Locked File');
#        chomp(my $signal = <PIDFILE>);
#        my_print('Got signal');
#          if ($signal eq "HUP") {
#            $signalHandlerMsg = $NOTIFIED;
#          } elsif ($signal eq "TERM"){
#            $signalHandlerMsg = $TERMINATED;
#          }else{
#            #IGNORE
#            my_print 'IGNORED:'.$signal;
#          }
#          
#        seek PIDFILE, 0, 0;
#          truncate PIDFILE, 0;
#        close (PIDFILE);
#    }
#  }else{
#
#  my $rout;
#  select($rout = $readFiles, undef, undef, $wakeupTime);
#  my_print("after select");
#  my $vec_output = vec($rout, fileno(READER), 1);
#  my_print("vec_output:$vec_output");
#  $vec_output and sysread READER, $signalHandlerMsg, 1;
#  }
#  my_print("SignalHandlerMsg:$signalHandlerMsg");
#  my_print("last statement");
#}
#
#my_print( "Process Manager terminated: pid=$$");
#
#unlink "$procDir/.procmgr.$$";
#
#exit 0;

sub lockFile
{
  my $filehandle = shift;

  # flock can fail due to a signal (e.g. SIGCHLD) so keep trying until
  # we succeed or we have tried a ridiculously large number of times
  flock($filehandle, LOCK_EX) and return 1 for 1..1000;
  return 0;
}

sub signalProcessManagers
{
  local $_;
  opendir(DIR, $PROC_DIR) or die "Can't open $PROC_DIR: $!\n";
  /^\.procmgr\.(\d+)$/ and kill 'HUP', $1 for readdir(DIR);
  closedir(DIR);
}

sub processManagerRunning
{
  local $_;
  my $running = 0;
  opendir(DIR, $PROC_DIR) or die "Can't open $PROC_DIR: $!\n";
  /^\.procmgr\.(\d+)$/ and kill 0, $1 and $running = 1 for readdir(DIR);
  closedir(DIR);
  return $running;
}

#foreach (keys %SIG) { print "$_\n" };

#my $count = numActiveManagers('D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch\process');
#my_print($count);
# Cleanup pid files and return number of active managers
sub numActiveManagers
{
  local $_;
  my $procDir = shift;

  opendir(DIR, $procDir) or fatal("Can't open $procDir: $!\n");
  my @procMgrPids = map { /\.(\d+)$/; $1 } grep { /^\.procmgr\.(\d+)$/ } readdir(DIR);
  closedir(DIR);

  my %status;
  $status{$_} = kill 0, $_ for @procMgrPids;

  # Remove old files
  my $count = keys %status;
  for (grep { $status{$_} == 0 } keys %status) {
    my_print( "Process manager ($_) died");
    unlink "$procDir/.procmgr.$_" or my_print( "Problem removing $procDir/.procmgr.$_: $!");
    $count--;
  }

  return $count;
}