#!/usr/bin/perl -w
use strict;

print(time);
print("\n===\n");
print(localtime(time));
print("\n===\n");
my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime(time);
print sprintf("%d/%02d/%02d %02d:%02d:%02d [$$]:", 1900+$year, $mon+1, $mday, $hour, $min, $sec);
print("\n===\n");

print(int(rand(10000)));

