#!/usr/bin/perl -w
use strict;
use base 'Exporter';
use Carp qw (confess croak );
use Term::Cap;


our @EXPORT = qw ( );
our @EXPORT_OK = qw ( clear_screen get_terminal_entry );

clear_screen();

sub clear_screen {
  #if($^O =~ /Win32/){
  # #system("cls");
  #  return;
  #}
  my $terminal = eval {
    get_terminal_entry();
  };
  if ($@) {
    # GEN-9412 - If we don't know how to clear the screen, just ignore it, don't fail
    print $@;
    return;
  }
  $terminal->Tputs('cl', 1, *STDOUT);
 #$terminal->Tgoto("cm", 2, 5); #

  return;
}

sub get_terminal_entry {
  # Query the local termcap database 
  # As per the Term::Cap perldoc, Tgetent returns a blessed object reference which the 
  # user can then use to send the control strings to the terminal using Tputs and Tgoto.
  print("YJ1\n");
  my $OSPEED = eval {
    require POSIX;
    my $termios = POSIX::Termios->new();
    $termios->getattr;
    return $termios->getospeed;
  };
  if (!defined($OSPEED)) {
    $OSPEED = 9600;
  }
   print("YJ2:$OSPEED\n");
  # In some cases, the $ENV{'TERM'} value for the given OS doesn't have an entry in
  # the local termcap database (i.e /etc/termcap).
  # If this occurs, we then force our $ENV{'TERM'} value to be vt100 as this should always work
  my $terminal;
  my $counter = 0;
  GET_TERMCAP: {
    my $term = $ENV{'TERM'} || "vt100";
    print("looking up termcap entry for TERM=${term}\n") ;
    $terminal = eval {
      if($^O =~ /Win32/){
        Term::Cap->Tgetent({ OSPEED=>$OSPEED,TERM => undef});
      }else{
        Term::Cap->Tgetent({ OSPEED=>$OSPEED, TERM => $term });
      }
    };
    if ($@) {
      if (($@ =~ /(failed termcap lookup|find a valid termcap file)/) && ($counter < 1)) {
        print $@;
        print("no termcap entry found for TERM=${term}, trying vt100\n") ;
        $ENV{'TERM'} = "vt100";
        $counter++;
        redo GET_TERMCAP;
      }
      else {
        confess "Error - " . $@;
      }
    }
  };

  confess "Unable to determine termcap entry for current terminal" if !defined($terminal);
  return $terminal;
}

1;
