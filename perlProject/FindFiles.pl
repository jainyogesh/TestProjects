#!/usr/bin/perl -w
use strict; 
use warnings;
use File::Find;
use Carp qw (confess croak );

my $dir = 'C:/YJ/Codebase/APSF-141Drop2/Modules/Tokenization';

find({ wanted => \&process_file}, $dir);

sub process_file {
  if ($File::Find::name =~ /-SNAPSHOT.jar/){
    my $name = $_;
    $name =~ s/-1.4.[1-9]-SNAPSHOT.jar//g;
    print "'".$name. "',";
  }
}



