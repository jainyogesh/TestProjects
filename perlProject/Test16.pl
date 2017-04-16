#!/usr/bin/perl -w
use strict;

use POSIX qw(setsid);
use POSIX qw(:sys_wait_h);


# Construct a pipe that our signal handler can use
# to wake up the main loop when it receives a SIGHUP
pipe(READER, WRITER);
WRITER->autoflush();

my $NOTIFIED   = "1";
my $CHILD_DIED = "2";
my $TERMINATED = "3";

$SIG{HUP}  = sub { syswrite WRITER, $NOTIFIED, 1; };
$SIG{CHLD} = sub {
  my_print("Got SIGCHLD");
  while ((my $pid = waitpid(-1, WNOHANG)) > 0) {
    my_print("Inside waitpid loop with PID: $pid");
    open(EXITFILE, ">$pid.exit");
    # Exit code is -signal number if killed else exit code of process
    my $exitCode = $? & 0xff ? -1 * ($? & 0xff) : ($? >> 8);
    print EXITFILE "$exitCode\n";
    close(EXITFILE);
  }
  syswrite WRITER, $CHILD_DIED, 1;
};

$SIG{TERM} = sub { syswrite WRITER, $TERMINATED, 1; };

$SIG{QUIT} = 'IGNORE';
$SIG{INT}  = 'IGNORE';


my $readFiles = '';
vec($readFiles, fileno(READER), 1) = 1;

my $signalHandlerMsg;

while (not $signalHandlerMsg eq $TERMINATED)
{
  my @files;
  my @procMgrPids;

  #open(PROC_LOCK, ">>$procDir/.lock") or do { logMsg(ERROR, "Couldn't open $procDir/.lock: $!"); 
  #                                       sleep 5; next; };
  #lockFile(*PROC_LOCK)                or do { logMsg(ERROR, "flock failed locking $procDir/.lock: $!");
  #                                       close(PROC_LOCK); sleep 5; next; };
  #
  #opendir(DIR, $procDir) or fatal("Can't open $procDir: $!\n");
  #for (readdir(DIR)) {
  #  /^[^\.].*\.proc$/ and push @files, $_;
  #}
  #closedir(DIR);
  #
  # Make sure all procmgrs are still running and clean up any
  # files for ones that are dead
  #manageProcessManagers($procDir, $NUM_PROCESS_MANAGERS);
  #
  # Get files in a random order so multiple instances of process managers
  # will hopefully suffer less contention for file locks
  #my @randFiles = map { $_->[0] } sort { $a->[1] <=> $b->[1] } map { [$_, rand] } @files;
  #
  # Keep track of when we should next wake up
  #my $firstWakeupTime;
  #for (@randFiles) {
  #  my $waketime = processFile("$procDir/$_", *PROC_LOCK);
  #  $firstWakeupTime = $waketime if
  #    $waketime and (not defined $firstWakeupTime or $waketime < $firstWakeupTime);
  #}
  #
  #my $wakeupTime = ($firstWakeupTime ? $firstWakeupTime : 1 + rand($DEFAULT_WAKEUP_TIME - 1));
  #
  # Clean up any .exit files that were written over 10 seconds ago
  #time - (stat($_))[9] > 10 and unlink $_ for <$procDir/*.exit>;
  #
  # Release our lock
  #close(PROC_LOCK);

  my $rout;
  select($rout = $readFiles, undef, undef, $wakeupTime);
  vec($rout, fileno(READER), 1) and sysread READER, $signalHandlerMsg, 1;
}

#logMsg(INFO, "Process Manager terminated: pid=$$");

#unlink "$procDir/.procmgr.$$";

exit 0;