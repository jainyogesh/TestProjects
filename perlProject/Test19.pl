#!/usr/bin/perl -w
use strict;


BEGIN {
    Win32::SetChildShowWindow(0)
        if defined &Win32::SetChildShowWindow
};

my $pid = '6516';

while (1) {
    my $result = isProcessRunning($pid);
    #isProcessRunning("3184");
    #isProcessRunning("1736");
    #print($result."\n");
}

sub my_print{
    print(shift . "\n");
}

sub isProcessRunning{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $command = 'tasklist /FI "PID eq '. $pid. '"';
    #print $command."\n";
    return (`$command` =~ /$pid/) ? 1 : 0;
   }else{
    return kill 0 ,$pid;
   }
}
