#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );

my $value = 'foo.bar';
my $packed_ip = gethostbyname($value);
confess "The specified host does not resolve" unless (defined($packed_ip)) ;