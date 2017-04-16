#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );
use File::Basename;
use File::Copy;
use File::Path qw( mkpath );
use File::Temp qw( tempfile );
use File::Path qw(make_path);

sub get_symlink_target {
  my ($arg_ref) = @_;
  
  confess "No filename defined" if !exists($arg_ref->{'filename'});
  my $filename = $arg_ref->{'filename'};
  
  #confess "The specified file is not a symbolic link" if (! -l $filename);

  my $file_base_directory = dirname($filename);
  my $ file_complete_name = basename($filename);
  
  print($file_base_directory. "\n");
  print($file_complete_name ."\n");
  my $target_filename;
  if ($^O =~ /Win32/) {
    # Check if $target_filename is symbolic link
    my ($status, $stdout, $stderr) = exec_cmd2({
      command => 'fsutil reparsepoint query "'. $filename .'" | find "Symbolic Link" >nul && echo symbolic link found || echo No symbolic link'
    });
    if ($stdout !~ /symbolic link found/) {
        confess "The specified file is not a symbolic link" if (! -l $filename);
    }
    
     $target_filename = `dir $file_base_directory /AL | Find "$file_complete_name"`;
  $target_filename =~ s/.*\[//;
  $target_filename =~ s/\]$//;
  }else{
  $target_filename = readlink($filename);
  }

  # GEN-4966
  # If a user has created a symlink manually and the symlink contains a trailing slash, things can break
  # so we remove the trailing slash here
  if ($target_filename =~ /\/$/) {
    $target_filename =~ s/\/$//;
  }

  if ($^O =~ /Win32/) {
    unless ($target_filename =~ /:/) {
    if (defined($file_base_directory)) {
      $target_filename = $file_base_directory . "\\" . $target_filename;
    }
  }
  }else{
  unless ($target_filename =~ /^\//) {
    if (defined($file_base_directory)) {
      $target_filename = $file_base_directory . "/" . $target_filename;
    }
  }
  }

  confess "Couldn't determine target file for symlink $filename" if (!defined($target_filename));
  return $target_filename;
}

my $destination = ($^O =~ /Win32/)?'D:\YJ\Temp\TestLink\TestLink2':'/tmp/TestLink/GrandChildLinuxLink';
my $source = ($^O =~ /Win32/)?'D:\YJ\Temp\TestLink\Parent\Child\GrandChild':'/tmp/TestLink/Parent/Child/GrandChild';
my $result = get_symlink_target({
    filename => $destination
});

print($result . "\n");
#if ( -l $destination ){
#    print('Windows symlink is considered');
#    unlink $destination ;
#}
#if ($^O =~ /Win32/) {
#      eval{`mklink /D $destination $source`};
#      if ($@) {
#        confess "Error creating symlink from $source to $destination - " . $@;
#      }
#    }else{
#      if (!symlink($source, $destination)) {
#        confess "Error creating symlink from $source to $destination - " . $@;
#      }
#}

