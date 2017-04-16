#!/usr/bin/perl -w
use strict;
use lib ('D:\YJ\UPP\UPP3.2\WindowsSupport\UPP-2.5-SNAPSHOT-installer\src\lib\perl');
use Distra::Switch::Env qw ( set_env );
use Distra::Common;

for(my $i =0 ; $i<1 ;$i++){
#my $cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -cp C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.platform.PlatformToEnv C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/instances/switch1/output/switch1/PlatformDefinition.txt '.$i;
my $cmd = 'java -cp D:\YJ\Temp\mcas_test UserInputTest';
my ($status, $key_value_format, $stderr);
eval{
  #($status, $key_value_format, $stderr) = exec_cmd2({
  #             command => $cmd,
  #             suppress_debug => 0
  #          });
  $key_value_format = exec_cmd_suppress_stdin({
    command => $cmd,
    stdin_value => 'Perl Works!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!'
  });
};
if ($@) {
  confess $@; 
}elsif (!defined($key_value_format)) {
  die "Unable to convert platform file to key/value pairs";
}
my @pairs = split(/\n/, $key_value_format);
my $size = @pairs;
if ($size != 50) {
  print("$size\n");
  print($status."\n");
  print($key_value_format."\n");
  print($stderr."\n");
  die("Size is wrong");
}


print("$i\n");
}
