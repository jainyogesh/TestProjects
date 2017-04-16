#!/usr/bin/perl -w
use strict;
use lib('D:\YJ\UPP\UPP3.2\WindowsSupport\UPP-2.5-SNAPSHOT-installer\src\lib\perl');
use Distra::Common;

my $java_cmd = 'java -version';
my $link_check = 'fsutil reparsepoint query "'. "D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/install/2.5-SNAPSHOT/executive/procmgr/a" .'" | find "Symbolic Link" >nul && echo symbolic link found || echo No symbolic link';
my $user_input_cmd = 'java -cp D:\YJ\Temp\mcas_test UserInputTest E';

my $cmd = $user_input_cmd;

for(my $i = 0; $i<99; $i++){
  my ($pid, $stdout, $stderr) = exec_cmd2({ command => "$cmd" });

  chomp($stdout);
  chomp($stderr);
  
  if (not defined($stdout) or length($stdout) == 0) {
    die "STDOUT is Empty";
  }
  
  
  
  if (not defined($stderr) or length($stderr) == 0) {
    die "STDERR is Empty";
  }
  

  print("STDOUT:$stdout");

  print("\n================\n");

  print("STDERR:$stderr:EXACTLY");
  
  print("\n================\n");
}