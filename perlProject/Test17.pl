#!/usr/bin/perl -w
use strict;
$| = 1;
main();
exit(0);
sub main{
while (1) {
    while (<STDIN>) {
      /^\s*$/ and next;
      do { error("Process manager not running"); next };
    }
    error("Outside stdin");
    eof STDIN and return;
}
}

sub ok
{
  my $msg = shift;
  print "OK" . (defined $msg ? " $msg\n" : "\n");
  return 1;
}

sub error
{
  my $msg = shift;
  print "ERROR" . ($msg ? " $msg\n" : "\n");
  return 0;
}