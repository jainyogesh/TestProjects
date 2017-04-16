#!/usr/bin/perl -w
use strict;
#$| = 1;
use Fcntl qw(:DEFAULT :flock);
my $the_file = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch\process\.procmgr.5716';
open(FH,'+<',"$the_file") || die("Error in opening file $!");;  # typical open call

while (1) {
  #print("Y0");
  my $out;
    while (<FH>) {
      $out  = $_;
      print($out);
      #$out .= $_;
      
      seek FH, 0, 0;
      truncate FH, 0;
    }
    #lockFile(*FH);
    #close(FH);
    #print ($out);
    # eof reached on FH, but wait a second and maybe there will be more output
    #sleep 1;
    seek FH, 0, 0;      # this clears the eof flag on FH
}


sub lockFile
{
  my $filehandle = shift;

  # flock can fail due to a signal (e.g. SIGCHLD) so keep trying until
  # we succeed or we have tried a ridiculously large number of times
  flock($filehandle, LOCK_EX) and return 1 for 1..1000;
  return 0;
}