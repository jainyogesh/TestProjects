#!/usr/bin/perl -w
use strict;

my $count = 0;

while ($count < 100) {
  $count++;
  my $status = isProcessRunning('1456');
  print ($status."\n");
}


sub isProcessRunning{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $command = 'tasklist /FI "PID eq '. $pid. '"';
    my $status = `$command`;
    
    #If we are not able to run tasklist for any reason we give it benifit of doubt and assume that process is running
    my $exit_value = $? >> 8;
    if ($exit_value != 0) {
      return 1;
    }
    
    return ($status =~ /$pid/) ? 1 : 0;
   }else{
    return kill 0 ,$pid;
   }
}