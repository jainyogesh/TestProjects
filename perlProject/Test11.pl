#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );


sub touch{
    my ($arg_ref) = @_;  
  confess "No filename defined" if !exists($arg_ref->{'filename'});
  my $filename = $arg_ref->{'filename'};
  
  if (! -e $filename){
    # create via append, which does not destroy existing file
    open  my $fh, '>>', $filename or confess "touch(): could not create $filename";
    close    $fh              or confess "touch(): could not create $filename";
  }else{
    confess "Cannot touch a directory" unless (-f $filename);
  }
  
  my $now = time();
    utime($now, $now, $filename) || confess "utime: $!";
  return;
}


touch({
       filename=>'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\log\switch1'
       });