#!/usr/bin/perl -w
use strict;

my $procDir = 'D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/executive/procmgr/process';

print($ENV{TZ}) if defined ($ENV{TZ});

$ENV{TZ} = 'Delhi/India';
print("TimeZone:$ENV{TZ}\n") if defined ($ENV{TZ});
print time."\n";
print (((stat($_))[9]) . "\n") for <$procDir/*.exit>;



#time - (stat($_))[9] > 10 and print $_ for <$procDir/*.exit>;