#!/usr/bin/perl -w
use strict;
use File::Basename;
use lib ('D:\YJ\UPP\UPP3.2\WindowsSupport\UPP-2.5-SNAPSHOT-installer\src\lib\perl');
use Distra::Common;
use Distra::File::Common qw ( get_symlink_target );

my $cmd = which_modifed('java');
#print $cmd."\n";

my $dir = dirname $cmd;
#print $dir ."\n";

#my $filename = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch\bin';
my $filename = 'C:\Program Files\del 1\Temp';
#$filename = get_short_path($filename) if $filename =~ / /;
my $target = get_symlink_target({
  'filename'=> $filename
});
print($target);















sub which_modifed {
  my $app = shift;
  return if !defined($app);

  my $location;
  if ($^O !~/Win/) {
    open(WHICH, "which $app 2>&1 |");
    while(<WHICH>) {
      chomp;
      $location = $_ if ($_ =~ /^\/([a-zA-Z0-9\_\-\.\/]+)$/);
    }
    close(WHICH);
  }else{
    $location = `where $app`;
    #remove \r.* | \n.* as we are only going to take first one
    $location=~ s/\r.*|\n.*//g;
    $location = get_short_path($location) if $location =~ / /;
    
  }
  return $location;
}