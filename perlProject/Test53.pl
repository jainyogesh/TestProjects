#!/usr/bin/perl -w
use strict;

my $execModule;
my $extension = ".pl";
my $fileAssoc = "assoc $extension";
my  $defaultProgram = `for /F "tokens=2 delims==" %a in ('$fileAssoc') do \@ftype %a`;
print $defaultProgram;
if ($defaultProgram =~ /=("*.*exe"*)/) {
    #Got the executable
    $execModule = $1;
}

print($execModule);