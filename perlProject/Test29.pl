#!/usr/bin/perl -w
use strict;


use strict;
use warnings;

use IO::Select qw( );
use IPC::Open3 qw( open3 );
use Socket     qw( AF_UNIX SOCK_STREAM PF_UNSPEC );

sub _pipe {
    socketpair($_[0], $_[1], AF_UNIX, SOCK_STREAM, PF_UNSPEC)
        or return undef;
    shutdown($_[0], 1);  # No more writing for reader
    shutdown($_[1], 0);  # No more reading for writer
    return 1;
}

sub _open3 {
    local (*TO_CHLD_R,     *TO_CHLD_W);
    local (*FR_CHLD_R,     *FR_CHLD_W);
    local (*FR_CHLD_ERR_R, *FR_CHLD_ERR_W);
    
    pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $!;
    if ($^O =~ /Win32/) {
        #_pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $^E;
    } else {
        #pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $!;
    }

    my $pid = open3('>&TO_CHLD_R', '<&FR_CHLD_W', '<&FR_CHLD_ERR_W', @_);
    return ( $pid, *TO_CHLD_W, *FR_CHLD_R, *FR_CHLD_ERR_R );
}

my $mcas_cmd = ($^O =~ /Win32/)?'java -cp D:\YJ\Temp\mcas_test UserInputTest E':'java -cp ~/YJ/Temp/mcas_test UserInputTest E';
my $password = 'PasswordInputIYJYJYJYJYJ';

#my $mcas_cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,address=upp1:5008 -Xmx128m -DJVM_COMPATIBILITY_CHECK_DISABLED=false -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.manage.util.MgmtCommandRunner DD-JNJY7BS 11301 admin';
#my $password = 'admin123';

my ($pid, $to_chld, $fr_chld, $fr_chld_err) =
    _open3($mcas_cmd);

my %objs;

my $in_sel  = IO::Select->new();
my $out_sel = IO::Select->new();

for my $fh ($fr_chld, $fr_chld_err) {
    my $obj = {
        buf => '',
    };
    $objs{ fileno($fh) } = $obj;
    $in_sel->add($fh);
}
print $to_chld "<<<>>>$password\n";
close($to_chld);
sleep 10;
while ($in_sel->count()) {
    my ($ins) = IO::Select::select($in_sel, undef, undef);

    for my $fh (@$ins) {
        my $obj = $objs{ fileno($fh) };
        our $buf; local *buf = \( $obj->{buf} );
        my $bytes_read = sysread($fh, $buf, 64*1024, length($buf));
        $in_sel->remove($fh) if (!$bytes_read);
        #if (!$bytes_read) {
        #    warn("Error reading from child: $!\n")
        #        if !defined($bytes_read);
        #    $in_sel->remove($fh);
        #}
    }
}

waitpid($pid, 0);

print("STDOUT:\n$objs{ fileno( $fr_chld     ) }{buf}");
print("\n");
print("STDERR:\n$objs{ fileno( $fr_chld_err ) }{buf}");