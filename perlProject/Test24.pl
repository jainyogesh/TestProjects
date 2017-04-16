#!/usr/bin/perl -w
use strict;


my $dir = ($^O =~ /Win32/)?'D:\YJ\Temp\linkTest\link':'/tmp/linkTest/link';

if (-e $dir) {
    print("$dir exists\n");
}else{
    print("$dir does not exists\n");
}

if (-w $dir) {
    print("$dir is writeable\n");
}else{
    print("$dir is not writeable\n");
}

if (-l $dir) {
    print("$dir is link\n");
    my $dest = readlink($dir);
    print("Destination is: $dest\n");
}else{
    print("$dir is not link\n");
}

#print(chdir($dir)."\n");

my $cmd = "cd $dir";
print($cmd."\n");
my $out = `$cmd`;
#print("output:$out\n");
