#!/usr/bin/perl -w
use strict;


my %dir = (
           'key' => 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\log\procmgr'
          );

for(values %dir){
 opendir(DIR, $_) or fatal("Can't open $_: $!\n");
for(readdir(DIR)){
    print ("$_\n");
}
close(DIR);
}
