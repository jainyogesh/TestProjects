#!/usr/bin/perl -w
use strict;
use Carp qw(confess croak);
my $env = 'PATH=D:\YJ\Softwares\Perl64\site\bin;D:\YJ\Softwares\Perl64\bin;C:\Program Files\RSA SecurID Token Common;D:\YJ\Softwares\klocwork\Server 9.2\bin;D:\YJ\Softwares\klocwork\User 9.2\bin;.;C:\Program Files\Java\jdk1.6.0_43\bin;D:\YJ\Softwares\apache\maven\3.1.1\bin;C:\windows\system32;C:\windows;C:\windows\System32\Wbem;C:\windows\System32\WindowsPowerShell\v1.0\;C:\Program Files\TortoiseSVN\bin;C:\Program Files (x86)\Common Files\Check Point\UIFramework 3.0\Bin\;C:\Program Files (x86)\CheckPoint\Endpoint Security\Endpoint Common\bin;D:\YJ\Softwares\scripts;D:\YJ\Softwares\Oracle\instantclient_11_2 TZ=Australia/Sydney';

my @vars = processEnvVars($env);
my $size = @vars;
print($size);
    @vars % 2 == 0 or confess "Error";
    while (@vars) {
      my $key = shift @vars;
      my $value = shift @vars;
      $ENV{$key} = $value;
    }



#foreach (@vars) {
#    /^([^=].*)=(.*)$/ or do { confess ("Trying to set environment with '$_'"); next; };
#    print($1."=".$2);
#}

#my @splitArray;
#while ($env =~ /([^ ]*=)/g) {
#    push @splitArray, $1;
#}



sub processEnvVars{
    local $_;
    my @envVars = split(/([^ ]*)=/,shift);
    my @envArray;
    while(@envVars) {
        my $envVar = shift @envVars;
        $envVar =~ s/^\s+|\s+$//g;
        if ($envVar ne "") {
            push @envArray, $envVar;
        }
    }
    return @envArray;
}

sub processArgs
{
  local $_;
  my @args = split(" ", shift);
  my @argArray;
  while (@args) {
    my $arg = shift @args;
    # Now deal with quoted arguments (both single and double)
    if ($arg =~ /^([^="]+=)?".*$/) {
      until ($arg =~ /("|".*[^\\])"$/ or not @args) {
        $arg .= " " . shift @args;
      }
      $arg =~ s/^([^="]+=)?"/$1/g;
      $arg =~ s/([^\\])"$/$1/g;
    }
    elsif ($arg =~ /^([^=']+=)?'.*$/) {
      until ($arg =~ /('|'.*[^\\])'$/ or not @args) {
        $arg .= " " . shift @args;
      }
      $arg =~ s/^([^=']+=)?'/$1/g;
      $arg =~ s/([^\\])'$/$1/g;
    }

    $arg =~ s/\\"/"/g;          # Change all quoted quotes to quotes
    $arg =~ s/\\'/'/g;
    push @argArray, $arg;
  }
  return @argArray;
}


