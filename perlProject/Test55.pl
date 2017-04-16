#!/usr/bin/perl -w
use strict;

BEGIN {
  Win32::SetChildShowWindow(0) if defined &Win32::SetChildShowWindow;
  if($^O =~ /Win32/)
      {
          require Win32::Process;
          import Win32::Process;
    }
};

my $DEBUG_PROCUTIL_FILE = "D:/YJ/UPP/UPP3.2/WindowsSupport/Platform1/procutils.log";
for(1..10){
  my $result =  isProcessRunning('5048');
  print $result."\n";
  
 # -f $DEBUG_PROCUTIL_FILE and open(DEBUG_LOG, ">> $DEBUG_PROCUTIL_FILE") and do { print DEBUG_LOG "Result:$result\n";  print DEBUG_LOG "Status:$result\n"; close(DEBUG_LOG) };
}

sub isProcessRunning{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $processObj;
    Win32::Process::Open($processObj,$pid,0) or do {
     -f $DEBUG_PROCUTIL_FILE and open(DEBUG_LOG, ">> $DEBUG_PROCUTIL_FILE") and do { 
        print DEBUG_LOG "$pid Object does not exists\n";
        close(DEBUG_LOG) 
      };
      return 0
    };
    if (defined $processObj){
      return 1;
    }else{
      -f $DEBUG_PROCUTIL_FILE and open(DEBUG_LOG, ">> $DEBUG_PROCUTIL_FILE") and do { 
        print DEBUG_LOG "$pid Object not defined \n";
        close(DEBUG_LOG) 
      };
      return 0;
    }
    
  } else{
    return kill 0 ,$pid;
   }
}