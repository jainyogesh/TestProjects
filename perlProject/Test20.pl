#!/usr/bin/perl -w
use strict;
use IPC::Open2;
#print($$);
#exec("wperl Test19.pl");

print("going to start process\n");
open2(*READI, *WRITEI, "Test17.pl") or die ("Cannot start process $!\n");

print ("Process started\n");
print WRITEI "WORKS!!\n";
print ("Written\n");
close (WRITEI);
my $result = "";
print ("Going inside loop\n");
while (<READI>) {
    print ("In loop..\n");
    $result .= $_;
    last if /^[^\.]/;
}
print ("Outsied loop\n");

close (READI);


print ("RESULT:$result\n");
exit(0);

#my $VERBOSE = 1;
#for ($ARGV[0]) {
#    do { my $status = procadmin($ARGV[1]);
#                      #print "$status\n";
#                      last; };
#}
#
#
#
#sub procadmin
#{
#  local $_;
#  my $command = shift;
#
#  
#
#  open2(*READ_PROCADMIN, *WRITE_PROCADMIN, "Test17.pl") or return "ERROR couldn't invoke procadmin";
#
#  print "$command\n" if $VERBOSE;
#  print WRITE_PROCADMIN "$command\n";
#  my $result = "";
#  while (<READ_PROCADMIN>) {
#    $result .= $_;
#    last if /^[^\.]/;
#  }
#  close(READ_PROCADMIN);
#  close(WRITE_PROCADMIN);
#  print "RESULT:\n$result" if $VERBOSE;
#  return $result;
#}