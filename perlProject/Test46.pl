#!/usr/bin/perl -w
use strict;
use IPC::Open3;
use File::Temp qw(tempfile);
$File::Temp::KEEP_ALL = 1;
#my $cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -cp C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.platform.PlatformToEnv C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/instances/switch1/output/switch1/PlatformDefinition.txt YJ2';

my $cmd = 'java -cp D:\YJ\Temp\mcas_test UserInputTest E';
my $fh_out = File::Temp->new();
my $out_name = $fh_out->filename;
print($out_name."\n");
close($fh_out);
#open ($fh_out, '+>','D:\Temp\stdout.txt');
my $fh_err = File::Temp->new();
my $err_name = $fh_err->filename;
print($err_name."\n");
close($fh_err);
#open($fh_err, '+>','D:\Temp\stderr.txt');
eval{
  system("$cmd 1>$out_name 2>$err_name") == 0 or die "Error in executing cmd: $!";;
};
my $error = $@;
if ($error) {
  die $error;
}

my $stdout = '';
open(OUT,'<', $out_name) or die "Error in reading $!";
while (<OUT>) {
  $stdout .= $_;
}
close(OUT);

my $stderr = '';
open(ERR,'<', $err_name) or die "Error in reading $!";
while (<ERR>) {
  $stderr .= $_;
}
close(ERR);

print("STDOUT:$stdout");
print("\n=============================\n");
print("STDERR:$stderr");

