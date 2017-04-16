#!/usr/bin/perl -w
use strict;
use base 'Exporter';
use Carp qw ( confess croak );

our %db2_info = (
    'view_exists'          => "select 1 from syscat.tables where tabschema=Upper('" .  '${database_username}' .  "') and tabname='"  .  '${name}'  .  "' and type='V'",
);

my $database_username = 'uppdev';
my $name = 'mcas_properties_ndv';
my $sql = eval( '"' . $db2_info{'view_exists'} . '"');

 if ($@) {
    confess "Error generating sql to check if view exists - " . $@;
  }

print $sql;