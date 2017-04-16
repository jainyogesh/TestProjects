#!/usr/bin/perl -w
use strict;
use v5.12;
use POSIX;

my $termios = POSIX::Termios->new(\*STDIN);
$termios->getattr;
my $lflag = $termios->getlflag;
$termios->setlflag($lflag & ~(&POSIX::ICANON) );
$termios->setattr;

my $pwd;
while (my $c = getc STDIN)
{
	last if $c eq "\n";
	print "\r \r";
	$pwd .= $c;
}

$termios->setlflag($lflag);
$termios->setattr;

warn "GOT: $pwd\n";

