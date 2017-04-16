#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );


my $path = 'C:\PROGRA~1\Java\JDK16~1.0_4';
$path =~ s/\\/\//g;
print("$path\n");

if (-x "C:/PROGRA~1/Java/JDK16~1.0_4/bin//keytool.exe") {
      print("present\n");
}else{
    print("absent\n");
}
my $exit_value = 19304 >> 8;
#print($exit_value);
print(256 >> 8);