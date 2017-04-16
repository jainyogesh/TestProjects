#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );

sub file_contains {
  return (file_grep_count(@_) > 0 ? 1:0);
}

sub file_grep_count {
    my $size = file_grep(@_);
    return $size;
}

sub file_grep {
    my ($arg_ref) = @_;

  confess "No filename was defined" if !exists($arg_ref->{'filename'});
  confess "No pattern was defined" if !exists($arg_ref->{'pattern'});

  my $filename = $arg_ref->{'filename'};
  my $pattern = $arg_ref->{'pattern'};

  my $count = 0;
  my @matches;
  if ( -e $filename) {
    open(FILE,"<", "$filename") or croak "Unable to open file: $filename - $!";
    while(<FILE>) {
      if ($_ =~ /$pattern/) {
        $matches[$count] = $_;
        $count ++;
      }
    }
    close(FILE) or croak "Unable to close file: $filename - $!";
  }
  return @matches;
}

my $filename = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch\install\2.5-SNAPSHOT\etc/distra.conf';
my $pattern = '^#Linux\$';

print("file_contains: ".file_contains({filename => $filename,
                     pattern => $pattern
                    })."\n");

print("file_grep_count: ".file_grep_count({filename => $filename,
                     pattern => $pattern
                    })."\n");

my @matches = file_grep({filename => $filename,
                     pattern => $pattern
                    });


print(@matches);

