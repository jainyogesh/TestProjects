#!/usr/bin/perl -w
use strict;
use IPC::Open3;
my $in = '';
use IO::File;
local *CATCHERR = IO::File->new_tmpfile;
my $cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -cp C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.platform.PlatformToEnv C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/instances/switch1/output/switch1/PlatformDefinition.txt YJ2';
my $pid = open3($in, \*CATCHOUT, ">&CATCHERR", $cmd);
sleep 5;
my $stdout = '';
while( <CATCHOUT> ) {
  $stdout .= $_;
}
waitpid($pid, 0);
seek CATCHERR, 0, 0;
my $stderr = '';
while( <CATCHERR> ) {
  $stderr .= $_;
}
$stdout =~ s/\r//g;
print("STDOUT:$stdout");
print("\n=============================\n");
print("STDERR:$stderr");

