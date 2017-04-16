#!/usr/bin/perl -w
use strict;


print("Starting..\n");
my $pid = fork() ;
if ($pid) {
  #Parent
  print STDOUT "Parent:$pid\n";
  #while(1){
  #  print "Parent test\n";
  #  sleep 1;
  #}
}else{
  #Child
  print("Child:$pid\n");
  #while(1){
  #  print "Child test\n";
  #  sleep 1;
  #}
}

#$pid = fork();
#if( $pid == 0 ){
#   print "This is child process\n";
#   print "Child process is existing\n";
#   exit 0;
#}
#print "This is parent process and child ID is $pid\n";
#print "Parent process is existing\n";
#exit 0;