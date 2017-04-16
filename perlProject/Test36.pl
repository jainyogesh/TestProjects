#!/usr/bin/perl -w
use strict;
use lib ('D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/lib/perl');
use strict;
use base 'Exporter';
use Carp qw (confess croak );
use threads;
use threads::shared;
use Distra::Common;
use Distra::Switch::Common;
use Distra::System::OS qw ( debug_os generate_stack_dump );
use Distra::File::Log;
use IPC::Open3;
use IO::Select;
use Symbol;
use Socket;
use IO::Handle;
use POSIX qw( WNOHANG );


sub _mcas_cmd_interactive_win {
  # This function runs mcas in interactive mode
  # Because the MCAS Management Command Runner prompts for the mcas password, and then echoes STDIN back to STDOUT,
  # we set up what is basically a proxy between the end-user and MCAS. This means that when the user is prompted for
  # the password, we are able to silently send this back to the Management Command Runner, thus avoiding it being
  # displayed on the screen and thus keeping PCi compliance.
  #
  # As we are effectively a proxy for communication with mcas, we also take the opportunity to log any keystrokes into
  # our log for future reference.
  $log->debug("YJ1");
  my ($arg_ref) = @_;

  confess "No mcas command defined" if !exists($arg_ref->{'command'});
  confess "No mcas password provided" if !exists($arg_ref->{'password'});

  my $mcas_cmd_interactive_command = $arg_ref->{'command'};

  my $password = $arg_ref->{'password'};
  
  my $select = IO::Select->new();
  
  my ($pid,$to_chld, $fr_chld, $fr_chld_err) = pipe_open3($mcas_cmd_interactive_command);
  $select->add($fr_chld,$fr_chld_err);
  
  my $stdin;
  local (*STDIN_R,*STDIN_W);
  socketpair_pipe(*STDIN_R,*STDIN_W) or die $^E;
  STDIN_W->autoflush(1);
  my $thr = threads->new(\&read_in_stdin,\*STDIN_W)->detach;
  $stdin = *STDIN_R;
  $select->add($stdin);
  
  my $stdout_removed = 0;
  my $stderr_removed = 0;
  while(my @ready =  $select->can_read) {
  foreach my $fh (@ready) {
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
}