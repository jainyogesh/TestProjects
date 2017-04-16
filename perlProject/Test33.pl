#!/usr/bin/perl -w
use warnings;
use strict;
#use Term::ReadKey;
use threads;

$|++;

#ReadMode('cbreak');

# works non-blocking if read stdin is in a thread
my $count = 0;
my $thr = threads->new(\&read_in_stdin)->detach;
print "thread started\n";
while(1){
print "test\n";
sleep 1;
}

#ReadMode('normal'); # restore normal tty settings


sub read_in{
        while(1){
          #my $stdin_buffer;
          my $char;
          while(defined ($char = ReadKey(0)) ) {
            print "\t\t$char->", ord($char),"\n";    
            #process key presses here
            #$stdin_buffer .= $char;
            if($char eq 'q'){exit}
            #if(length $char){exit}  # panic button on any key :-)
          }
          #print $stdin_buffer."\n" if defined $stdin_buffer;
        }
}

sub read_in_stdin{
        while(<STDIN>){
          my $input =  $_;
          print "GOT:$input:exactly\n";
          
          $input =~ s/\n|\r//g;
          print "PARSED:$input:exactly\n";
          if( $input eq 'quit'){exit}
        }
}