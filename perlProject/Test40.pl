#!/usr/bin/perl -w
use strict;




print("YJ");
my $file = 'D:\YJ\Temp\procmgr_thread.log';
my $DEBUG_FILE = 'D:\YJ\Temp\procmgr_thread_debugg.log';


# This logic has been introduced because it is possible that previous signal is still being
# processed by proc managers. In such scenario, we try 3 times waiting for 1 second every time
# and check if file is empty. If file is not empty by then, we just ignore this new signal
# It is not a very good logic but the only other alternative would be to override previous signal
my $count = 0;
my $file_stat = -z $file;
print( -s $file );
while (-s $file) {
  # Increment the count
  -f $DEBUG_FILE and open(DEBUG_LOG, ">> $DEBUG_FILE") and do { print DEBUG_LOG "File size -s $file \n";  close(DEBUG_LOG) };
  print("YJ1\n");
  $count++;
  # Sleep for 1 second as file is not empty and older signal is being processed
  sleep 1;
}
