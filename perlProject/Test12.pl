#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );
use File::Basename;
use File::Copy;
use File::Path qw( mkpath );
use File::Temp qw( tempfile );
use File::Path qw(make_path);

sub my_print {
    my $out = shift;
    print($out."\n");
}

sub get_file_contents {
  my ($arg_ref) = @_;

  confess "No file to open defined" unless exists($arg_ref->{'filename'});
  confess "The specified file [" . $arg_ref->{'filename'} . "] does not exist" unless ( -f $arg_ref->{'filename'});

  my $filename = $arg_ref->{'filename'};

  my $file_contents;
  if ( -e $filename) {
    open(FILE,"<", "$filename") or croak "Unable to open file: $filename - $!";
    while(<FILE>) {
      $file_contents .= $_;
    }
    close(FILE) or croak "Unable to close file: $filename - $!";;
  } 
  return $file_contents;
}

sub copy_file {
  my ($arg_ref) = @_;

  confess "No source filename defined" if !exists($arg_ref->{'source_filename'});
  confess "The specified source file does not exist" if (! -e $arg_ref->{'source_filename'});
  confess "No destination filename defined" if !exists($arg_ref->{'destination_filename'});

  my $source_filename = $arg_ref->{'source_filename'};
  my $destination_filename = $arg_ref->{'destination_filename'};

  my_print("copy_file: ${source_filename} -> ${destination_filename}");

  my($basename, $dir, $suffix) = fileparse($destination_filename, qr/\..*/);

  # check destination dir exists
  if ( ! -d $dir) {
    mkpath($dir) or confess "$!";
  }
  my_print($suffix);
 if (($^O =~ /Win32/) && (!defined $suffix || $suffix eq '') &&($dir =~ /bin/)) {
    eval{
        my $file_contents = get_file_contents({
            filename=>$source_filename
        });
        if ($file_contents =~ /#!.*\/perl/) {
            $destination_filename .= '.pl';
        }
        
    }
  }
  

  my_print("copy_file: ${source_filename} -> ${destination_filename}");
  return;
}

copy_file({
    source_filename=>'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP-2.5-SNAPSHOT-installer\src\executive\bin\init.pl',
    destination_filename=>'D:\YJ\Temp\Copy_Test\bin\init.pl'
});