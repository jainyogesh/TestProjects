#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );

sub is_windows_os {
    my $perl_os_name = $^O;
     if($perl_os_name =~ /Win/){
     return 1;
  }
  return 0;
}

sub validateDirectoryMustContainFile {
  my ($directory, $file) = @_;
  if(is_windows_os()){
    my $exefile = $file . ".exe";
    confess "The specified directory is invalid because $directory/$file or $directory/$exefile does not exist" unless ( -e "$directory/$exefile" || -e "$directory/$file");
  } else {
    confess "The specified directory is invalid because $directory/$file does not exist" unless ( -e "$directory/$file");
  }
}

sub file_regex {
  print("start file_regex\n");
  # Used to replace a variable with a value in a given file
  my ($arg_ref) = @_;

  confess "No filename defined" if !exists($arg_ref->{'filename'});
  confess "No variable defined" if !exists($arg_ref->{'variable'});
  confess "No value defined" if !exists($arg_ref->{'value'});

  my $filename = $arg_ref->{'filename'};
  my $variable = $arg_ref->{'variable'};
  my $value = $arg_ref->{'value'};
  print("file $filename\n");
  if (! -e $filename) {
    confess "The specified filename does not exist";
  }

  system("perl -pi.bak -e \"s#$variable#$value#g\" $filename");
  unlink($filename.".bak");
  print("end file_regex\n");
  return;
}

sub file_regex_expression{
  # Used to execute regular expression
  my ($arg_ref) = @_;
  
  confess "No filename defined" if !exists($arg_ref->{'filename'});
  confess "No expression defined" if !exists($arg_ref->{'expression'});

  my $filename = $arg_ref->{'filename'};
  my $expression = $arg_ref->{'expression'};
  my $processed;
  if (! -e $filename) {
    confess "The specified filename does not exist";
  }
  if (is_windows_os()) {
    print("perl -pi.bak -e \"$expression\" $filename\n");
    $processed = system("perl -pi.bak -e \"$expression\" $filename");
    unlink($filename.".bak");
  }else{
    $processed = system("perl -pi -e \"$expression\" $filename");
  }
  return $processed;
}

sub get_normalized_path {
  my $path = shift;
  $path =~ s/\\/\//g;
  return $path;
}


#validateDirectoryMustContainFile('C:\PROGRA~1\Java\JDK16~1.0_4','bin/nothtere');
my $parameter = "ORACLE_LIB";
my $value = "D:\\YJ\\Softwares\\Oracle\\instantclient_11_2";
$value =~ s/\/|\\/\\\//g;
#$value =~ s/\\/\\\\/g;
print("Value:$value\n");
my $base_directory = "D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT";
my $processed;
eval {
        $processed = file_regex_expression({
                           expression => "s/# User definitions\\n/# User definitions\\n" . $parameter . " = $value\\n/",
                           filename => "$base_directory/etc/distra.conf"});
      };
if ($@) {
  confess $@;
}
print($processed);


