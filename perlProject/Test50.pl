#!/usr/bin/perl -w
use strict;
my %ORIG_ENV = %ENV;
print "Original Environment:\n";
print "$_=" . $ORIG_ENV{$_} . "\n" for keys %ORIG_ENV;

print("\n#############################################################\n");

$ENV{YJ} = 'IS GREAT';
print "New Environment:\n";
print "$_=" . $ENV{$_} . "\n" for keys %ENV;

print("\n#############################################################\n");

print "Reverting Back\n";
%ENV = %ORIG_ENV;
print "$_=" . $ENV{$_} . "\n" for keys %ENV;
