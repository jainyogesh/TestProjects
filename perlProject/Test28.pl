#!/usr/bin/perl -w
use strict;

use lib ('D:\YJ\UPP\UPP3.2\WindowsSupport\UPP-2.5-SNAPSHOT-installer\src\lib\perl');
use base 'Exporter';
use Carp qw (confess croak );
use Distra::Common;
use Distra::Switch::Common;
use Distra::System::OS qw ( debug_os generate_stack_dump );
use Distra::File::Log;
use IPC::Open3;
use IO::Select;
use Symbol;
use Socket;
use IO::Handle;
use POSIX qw( WNOHANG );

my $mgmt_runner = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -DJVM_COMPATIBILITY_CHECK_DISABLED=false -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.manage.util.MgmtCommandRunner DD-JNJY7BS 11301 admin';
#my $cmd = 'mcas/platform listServices';
my $cmd = "mcas/config storeCartridge url=file://D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/amex-gcag-2.5-SNAPSHOT-cartridge.jar";
#my $mcas_cmd_win = $mgmt_runner . " " . $cmd;
my $mcas_cmd_win = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,address=upp1:5008 -Xmx128m -DJVM_COMPATIBILITY_CHECK_DISABLED=false -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.manage.util.MgmtCommandRunner DD-JNJY7BS 11301 admin';
my $mcas_cmd = ($^O =~ /Win32/)?$mcas_cmd_win:'java -cp ~/YJ/Temp/mcas_test UserInputTest E';
my $password = 'admin123';
  
  my $stdout;
  my $stderr;
  my $exit_code;

  my($child_pid,$to_chld,$fr_child,$fr_chld_err) = pipe_open3($mcas_cmd);

  local $SIG{CHLD} = sub {
    if ( ( waitpid($child_pid, 0) > 0) && ($? > 0) ) {
      confess "$stderr";
    }
  };

  print $to_chld $password. "\n";
  close($to_chld);

  my $selector = IO::Select->new();
  $selector->add($fr_child, $fr_chld_err);

  while (my @ready = $selector->can_read) {
    my $out;
    my $err;
    foreach my $fh (@ready) {
      my $buf;
      my $bufSize = 1; # We are just putting some size which seems to be reasonable
      my $bytes_read = sysread($fh, $buf,$bufSize);      
      if (fileno($fh) == fileno($fr_chld_err)) {
        $err .= $buf;
      } else {
        $out .= $buf;
      }
      if (!defined($bytes_read) && !$!{ECONNRESET}){
            # There was an error reading
           confess "Error from child: $!\n";
      }elsif (!$bytes_read){
           # Finished reading from this FH because we read
           # 0 bytes.  Remove this handle from $sel.  
           # we will exit the loop once we remove all file
           # handles ($outfh and $errfh).
           $selector->remove($fh);
           next;
         }
      #$selector->remove($fh) if (!$bytes_read);
    }
    
    if (defined $out){
        print("OUT:$out:EXACTLY\n");
        $stdout .= $out;
    }
    if (defined $err){
        print("ERR:$err:EXACTLY\n") ;
         $stderr .= $err;
    }
    
  }
  close $fr_child;
  close $fr_chld_err;
  
  print("STDOUT:".$stdout."\n") if defined $stdout;
  print("STDERR:".$stderr."\n") if defined $stderr;

  if (defined($stderr)) {
    if (length($stderr) > 0) {
      confess "$stderr";
    }
  }
  
  
  