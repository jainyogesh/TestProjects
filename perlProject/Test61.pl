#!/usr/bin/perl -w
use strict;
use base 'Exporter';
use Carp qw ( confess croak );
use POSIX qw[tzset];

$ENV{'TZ'} = 'PST8PDT';
#tzset();
print scalar localtime();

print "\n";

#$ENV{'TZ'} = 'Australia/Sydney';
#tzset();
#print scalar localtime();

print join("\n", map { s|/|::|g; s|\.pm$||; $_ } keys %INC);
