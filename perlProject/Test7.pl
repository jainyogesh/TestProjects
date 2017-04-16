#!/usr/bin/perl -w
use strict;
use base 'Exporter';
use Carp qw(confess croak carp);
use Carp::Heavy;
use IPC::Open3;
use IO::Handle;
use IO::Select;
use POSIX ":sys_wait_h";
use Symbol;

our $subStderr;
our $subStdout;
our $subExit;
our $exitValue;
our $scriptError;
our %execCmdEnv;
our $SUPPRESS_EXEC_CMD_DEBUG = 0;
our $SUPPRESS_STDOUT = 0;
my $log = '';
#use Distra::File::Log;
use Socket qw( AF_UNIX SOCK_STREAM PF_UNSPEC );

sub my_print {
    my $out = shift;
    print($out."\n");
}

# new version that returns (status, stdout, stderr)
# and lets caller decide if command failed
sub exec_cmd2 {
  my ($arg_ref) = @_;
  confess "No command defined" unless exists($arg_ref->{'command'});
  my $timeout = 0;
  if( exists($arg_ref->{'timeout'}) && defined($arg_ref->{'timeout'})) { $timeout = $arg_ref->{'timeout'}; }

  my $no_confess_on_error = 0;
  if( exists($arg_ref->{'no_confess_on_error'})) { $no_confess_on_error = $arg_ref->{'no_confess_on_error'}; }

  my $cmd = $arg_ref->{'command'};
  my $suppress_debug = $SUPPRESS_EXEC_CMD_DEBUG;
  $suppress_debug = $arg_ref->{'suppress_debug'} if exists($arg_ref->{'suppress_debug'});

  # export custom environment variables
  foreach my $key (keys %execCmdEnv) {
    $ENV{"$key"} = $execCmdEnv{$key};
    #my_print("exec_cmd: Custom environment variable: ${key}=" . $execCmdEnv{"$key"}  ) if ( defined($log) && (!$suppress_debug) );
  }

  # Log our environment variables
  foreach my $env (keys %ENV) {
    #my_print("exec_cmd: Environment variable: ${env}=" . $ENV{"$env"} ) if ( (defined($log)) && (exists($ENV{"$env"})) && (!$suppress_debug) );
  }

  my_print("exec_cmd: Executing command: $cmd") if ( defined($log) && (!$suppress_debug) );
  my_print("exec_cmd: Alarm timeout set to $timeout seconds") if ( (defined($timeout)) && (defined($log)) && (!$suppress_debug) );

# We could simplify this code greatly by using IPC::Open3::Utils, but lets not introduce a new dependency just now
#  my ($stdout, $stderr);
#  my $status = 0;
#  if (!put_cmd_in($cmd, \$stdout, \$stderr, {
#          'timeout' => $timeout,
#          'close_stdin' => 1
#      })) {
#    my_print("put_cmd_in failed: " . $!) if defined($log);
#  }
#  if ($@) {
#    my_print("put_cmd_in error: " . $@) if defined($log);
#  }
#  $status = $?;
#
#  my_print("put_cmd_in status: " . $status) if defined($log);
#  return ($status, $stdout, $stderr);


  # Based on 16.9 Controlling the Input Output and Error of Another Program from
  # The Perl Cookbook
  # and http://trevorbowen.com/tag/timeout

  my $stdout;
  my $stderr;
  my $pid;

  # Restore the DEFAULT signal handler for SIGCHLD so nobody else reaps our child
  # Since this is local, it will revert to whatever anyone else has set when we return
  local $SIG{CHLD} = 'DEFAULT';

  # Below code does not work with Windows so replacing it with mentioned here
  # http://www.perlmonks.org/index.pl?node_id=811650
  #eval {
  #  local $SIG{ALRM} = sub { die "TIMEOUT";};
  #
  #  $pid = open3(*CMD_IN, *CMD_OUT, *CMD_ERR, $cmd) || confess "Unable to exec cmd: $cmd";
  #  my_print("child pid: ${pid}") if ( defined($log) && (!$suppress_debug) );
  #
  # alarm($timeout);
  #
  #  my $selector = IO::Select->new();
  #  $selector->add(*CMD_OUT, *CMD_ERR);
  #
  #  while (my @ready = $selector->can_read) {
  #    foreach my $fh (@ready) {
  #      if (fileno($fh) == fileno(CMD_ERR)) {$stderr .= <CMD_ERR>;}
  #      else                                {$stdout .= <CMD_OUT>;}
  #      $selector->remove($fh) if eof($fh);
  #    }
  #  }
  #
  #  # cancel alarm
  #  alarm(0);
  #
  #  close(CMD_OUT);
  #  close(CMD_ERR);
  #};
  eval{
      local $SIG{ALRM} = sub { die "TIMEOUT";};
      my($to_chld, $fr_chld, $fr_chld_err);
      ($pid, $to_chld, $fr_chld, $fr_chld_err) = _open3($cmd);
      #Close $to_chld right away as we are not planning to write anything to process
      close($to_chld);
    
      alarm($timeout);
  
      my $in_sel  = IO::Select->new();
      $in_sel->add($fr_chld, $fr_chld_err);
  
      while (my @ready = $in_sel->can_read) {
        foreach my $fh (@ready) {
          if (fileno($fh) == fileno($fr_chld_err)) {$stderr .= <$fr_chld_err>;}
          else                                {$stdout .= <$fr_chld>;} 
          $in_sel->remove($fh) if eof($fh);
        }
      }
  
      # cancel alarm
      alarm(0);
      
      close($fr_chld);
      close($fr_chld_err);
  };

  my $error = $@;
  if ($error) {
    if ($error =~ /^TIMEOUT/) {
      # timeout - Make sure child is dead
      kill 9, $pid;
    }
    confess $error;
  }

  # get child status
  my $ret = waitpid($pid, 0);
  my $errno = $?;
  my_print("waitpid returned: ${ret}") if ( defined($log) && (!$suppress_debug) );
  my_print("errno: ${errno}") if ( defined($log) && (!$suppress_debug) );

  my $status = $?;
  my_print("status: ${status}") if ( defined($log) && (!$suppress_debug) );
  
  if (defined($stdout)) {
    #In Windows a Carriage Return is added at the end. Trimming the same.
    $stdout =~ s/\r//g;
    my_print("stdout: ${stdout}") if ( defined($log) && (!$suppress_debug) );
  }
  
  if (defined($stderr)) {
    #In Windows a Carriage Return is added at the end. Trimming the same.
    $stderr =~ s/\r//g;
    my_print("stderr: ${stderr}") if ( defined($log) && (!$suppress_debug) );
  }
  
  return ($status, $stdout, $stderr);
}

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
my $destination = ($^O =~ /Win32/)?'D:/YJ/Temp/TestLink/GrandChildWindowsLink':'/tmp/TestLink/GrandChildLinuxLink';
my $source = ($^O =~ /Win32/)?'D:/YJ/Temp/TestLink/Parent/Child/GrandChild':'/tmp/TestLink/Parent/Child/GrandChild';


my ($delStatus, $delStdout, $delStderr) = exec_cmd2({
    command => 'fsutil reparsepoint query "'. $destination .'" | find "Symbolic Link" >nul && echo symbolic link found || echo No symbolic link'
});

if ($delStdout =~ /symbolic link found/) {
    rmdir $destination or confess "symlink from $source to $destination exists and cannot be removed";
}

#For this command, we need to replace / with \ else it starts assuming it as an option
$destination =~ s/\//\\/g;
$source =~ s/\//\\/g;
my ($status, $stdout, $stderr) = exec_cmd2({
    command => 'mklink /D ' .$destination . ' ' .$source
});

if ($status >> 8 != 0) {
    my $error = "Error creating symlink from $source to $destination - ";
    if (defined $stderr){
        $error.= $stderr;
    }
    confess $error;
}


