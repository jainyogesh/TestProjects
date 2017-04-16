#!/usr/bin/perl -w
use strict;
use Carp qw (confess croak );



my $cmd_java = 'C:\Program Files\Java\jdk1.6.0_43/bin/java.exe';
$cmd_java = get_short_path($cmd_java) if ($cmd_java =~/ */);
print($cmd_java);


sub get_short_path {
    print("inside\n");
  my $long_path = shift;
  if($^O =~ /Win32/){
    #my ($status, $stdout, $stderr) = exec_cmd2({ command => 'for /d %I in ("'.$long_path.'") do @echo|set /p TEMP_LONG_PATH=%~sI' });
    #if (($status >> 8) != 0) {
    #     confess "Error getting short path for '$long_path':  $stderr";
    #}
    #
    #return get_normalized_path($stdout);
    my $command = 'for /d %I in ("'.$long_path.'") do @echo|set /p TEMP_LONG_PATH=%~sI';
    return `$command`;
  } else{
    return $long_path;
  }
}