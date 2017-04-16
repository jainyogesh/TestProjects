#!/usr/bin/perl -w
use strict;
use Carp qw ( confess croak );

our %technology_jdbc_info = (
  "SQLServer"      => {
    'clean'                => 'DECLARE \@sql NVARCHAR(MAX) = N\'\';'.
                              'SELECT \@sql += \'DROP TABLE \' + QUOTENAME(t.table_name) + \';\' FROM information_schema.tables AS t WHERE t.table_type = \'BASE TABLE\';'.
                              'EXEC sp_executesql \@sql;'.
                              '\ngo\n'.
                              'DECLARE \@sql NVARCHAR(MAX) = N\'\';'.
                              'SELECT \@sql += \'DROP VIEW \' + QUOTENAME(t.table_name) + \';\' FROM information_schema.tables AS t WHERE t.table_type = \'VIEW\';'.
                              'EXEC sp_executesql \@sql;',
    }
  );

my $sql = eval( '"' . $technology_jdbc_info{'SQLServer'}{'clean'} . '"');
if ($@) {
    confess $@;
}
print($sql);