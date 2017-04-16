#!/usr/bin/perl -w
use strict;

#%ENV = ();
#print("$_=" . $ENV{$_} . "\n") for keys %ENV;
my %vars;

  # Populate with environment first
  #$vars{$_} = $ENV{$_} for keys %ENV;
  #print("$_=" . $vars{$_} . "\n") for keys %vars;
  
  print($^O."\n");
  
  my $execModule = `where perl.exe`;
    #remove \r.* | \n.* as we are only going to take first one
    $execModule=~ s/\r.*|\n.*//g;
    
    print($execModule);