#!/usr/bin/perl -w
use strict;

use IPC::Open3;
use IO::Select;

my $win_cmd = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/java.exe -cp D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.security.util.Obfuscator 2';
my $linux_cmd = 'java -cp /home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar:/home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.security.util.Obfuscator 2';
my $cmd = ($^O =~ /Win32/) ? $win_cmd : $linux_cmd;

#my ($TO_CHLD_R,$TO_CHLD_W);
#local (*FR_CHLD_R,     *FR_CHLD_W);
#my (*FR_CHLD_ERR_R, *FR_CHLD_ERR_W);

my $pid = open3(*CMD_IN, *CMD_OUT, *CMD_ERR, $cmd);

$SIG{CHLD} = sub {
    print "REAPER: status $? on $pid\n" if waitpid($pid, 0) > 0
};

print CMD_IN "password\n";
close(CMD_IN);

my $selector = IO::Select->new();
$selector->add(*CMD_ERR, *CMD_OUT);

while (my @ready = $selector->can_read) {
    foreach my $fh (@ready) {
        if (fileno($fh) == fileno(CMD_ERR)) {print "STDERR: ", scalar <CMD_ERR>}
        else                                {print "STDOUT: ", scalar <CMD_OUT>}
        $selector->remove($fh) if eof($fh);
    }
}
close(CMD_OUT);
close(CMD_ERR);
