#!/usr/bin/perl -w
use strict;
use IPC::Open3;
use IO::File;
my $in = '';
local *CATCHOUT = IO::File->new_tmpfile;
local *CATCHERR = IO::File->new_tmpfile;
my $cmd = 'java -cp D:\YJ\Temp\mcas_test UserInputTest E';
my $pid = open3($in, ">&CATCHOUT", ">&CATCHERR", $cmd);
waitpid($pid, 0);
sleep 5;
seek $_, 0, 0 for \*CATCHOUT, \*CATCHERR;
my $stdout = '';
while( <CATCHOUT> ) {
  $stdout .= $_;
}
my $stderr = '';
while( <CATCHERR> ) {
  $stderr .= $_;
}

print("STDOUT:$stdout");
print("\n=============================\n");
print("STDERR:$stderr");