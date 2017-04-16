#!/usr/bin/perl -w
use strict;
use lib ('D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/lib/perl');
use Distra::File::Regex qw (file_regex_expression);

my $installer_resource_file = 'D:\YJ\Temp\switch.conf';

file_regex_expression({
                             filename => $installer_resource_file,
                             expression => 's/.*platform_monitor(\s *.+)*/#/g'
                             });
      
file_regex_expression({
                       filename => $installer_resource_file,
                       expression => 's/.*cpuload(\s .+)*/#/g'
                       });

