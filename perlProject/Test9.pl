#!/usr/bin/perl -w
use strict;
use base 'Exporter';
use Carp qw (confess croak );
use File::Basename;
use File::Copy;
use File::Path qw( mkpath );
use File::Temp qw( tempfile );
use File::Path qw(make_path);

sub copy_file {
  my ($arg_ref) = @_;
    print($arg_ref->{'source_filename'}."\n");
  confess "No source filename defined" if !exists($arg_ref->{'source_filename'});
  confess "The specified source file does not exist" if (! -e $arg_ref->{'source_filename'});
  confess "No destination filename defined" if !exists($arg_ref->{'destination_filename'});

  my $source_filename = $arg_ref->{'source_filename'};
  my $destination_filename = $arg_ref->{'destination_filename'};

  print("copy_file: ${source_filename} -> ${destination_filename}");

  my($basename, $dir, $suffix) = fileparse($destination_filename);

  # check destination dir exists
  if ( ! -d $dir) {
    mkpath($dir) or confess "$!";
  }

  copy( $source_filename, $destination_filename ) or confess "Copy failed: $!";
  return;
}

sub file_regex {
  # Used to replace a variable with a value in a given file
  my ($arg_ref) = @_;

  confess "No filename defined" if !exists($arg_ref->{'filename'});
  confess "No variable defined" if !exists($arg_ref->{'variable'});
  confess "No value defined" if !exists($arg_ref->{'value'});

  my $filename = $arg_ref->{'filename'};
  my $variable = $arg_ref->{'variable'};
  my $value = $arg_ref->{'value'};

  if (! -e $filename) {
    confess "The specified filename does not exist";
  }
    #$value = get_normalized_path($value);
    file_regex_expression({
                       expression => "s#$variable#$value#g",
                       filename => $filename
                     });

  return;
}

sub file_regex_expression{
  # Used to execute regular expression
  my ($arg_ref) = @_;
  
  confess "No filename defined" if !exists($arg_ref->{'filename'});
  confess "No expression defined" if !exists($arg_ref->{'expression'});

  my $filename = $arg_ref->{'filename'};
  my $expression = $arg_ref->{'expression'};
  my $exitStatus;
  if (! -e $filename) {
    confess "The specified filename does not exist";
  }
  if ($^O =~ /Win32/) {
    $exitStatus = system("perl -pi.bak -e \"$expression\" $filename");
    unlink($filename.".bak");
  }else{
    print("YJ1\n");
    $exitStatus = system("perl -pi -e \"$expression\" $filename");
  }
  return $exitStatus;
}

my $filename = (($^O =~ /Win32/)?'D:/YJ/Temp/Copy_Test_Windows.sql':'/tmp/Copy_Test_Linux_2.sql' );
my $tablespace = 'LOG';
copy_file({
           source_filename=>(($^O =~ /Win32/)?'D:':'/home/jainy' ) .'/YJ/UPP/UPP3.2/WindowsSupport/UPP-2.5-SNAPSHOT-installer/src/cartridges/oracle_no_tde_no_part/db_create.sql',
           destination_filename=>$filename
           });

file_regex({
        filename => $filename,
        variable => "<<DISTRA_TABLESPACE>>",
        value => $tablespace,
     });