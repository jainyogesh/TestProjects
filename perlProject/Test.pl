use strict;
use warnings;
use Carp qw (confess croak );
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

    if ($^O =~ /Win32/) {
        _pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $^E;
        _pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $^E;
    } else {
        pipe(*TO_CHLD_R,     *TO_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_R,     *FR_CHLD_W    ) or die $!;
        pipe(*FR_CHLD_ERR_R, *FR_CHLD_ERR_W) or die $!;
    }
    print ( @_);
    my $pid = open3('>&TO_CHLD_R', '<&FR_CHLD_W', '<&FR_CHLD_ERR_W', @_);

    return ( $pid, *TO_CHLD_W, *FR_CHLD_R, *FR_CHLD_ERR_R );
}

sub exec_cmd_suppress_stdin {
  print("Inside exec_cmd_suppress_stdin\n");
  my ($arg_ref) = @_;

  confess "No command defined" if ( !exists($arg_ref->{'command'}) || !defined($arg_ref->{'command'}) );
  confess "No stdin value defined" if ( !exists($arg_ref->{'stdin_value'}) || !defined($arg_ref->{'stdin_value'}) );

  my $stdin_value = $arg_ref->{'stdin_value'};
  my $command = $arg_ref->{'command'};
  my $timeout = $arg_ref->{'timeout'};

  my $stdout;
  my $stderr;
  my $exit_code;
  my $child_pid;

  my ($infh,$outfh,$errfh);
  #$errfh = gensym();

  # Setup our signal handlers
  local $SIG{CHLD} = sub {
    if (defined($child_pid)) {
      if ( ( waitpid($child_pid, 0) > 0) && ($? > 0) ) {
        if (defined($stderr)) {
          chomp $stderr;
          confess $stderr;
        }
      }
    }
  };

  local $SIG{ALRM} = sub {
    # Time out after $timeout seconds
    confess "Timed out after $timeout seconds executing command: $command";
  };
  print("just before eval\n");
  eval {
    #$child_pid = _open3($infh, $outfh, $errfh, $command);
    ($child_pid, $infh, $outfh, $errfh) = _open3($command);
    print $infh $stdin_value  . "\n";
    close ($infh);
  };
  print("just after eval\n");
  # Setup our alarm
  alarm $timeout if defined($timeout);

  my $sel = new IO::Select; # create a select object
  $sel->add($outfh,$errfh); # and add the fhs
  print("starting while loop now\n");
  # $sel->can_read will block until there is data available
  # on one or more fhs
  while(my @ready = $sel->can_read) {
    # now we have a list of all fhs that we can read from
    foreach my $fh (@ready) { # loop through them
      my $line;
      # read up to 4096 bytes from this fh.
      # if there is less than 4096 bytes, we'll only get
      # those available bytes and won't block.  If there
      # is more than 4096 bytes, we'll only read 4096 and
      # wait for the next iteration through the loop to
      # read the rest.
      my $len = sysread $fh, $line, 4096;
      if(not defined $len){
        # There was an error reading
        confess "Error from child: $!\n";
      } elsif ($len == 0){
        # Finished reading from this FH because we read
        # 0 bytes.  Remove this handle from $sel.
        # we will exit the loop once we remove all file
        # handles ($outfh and $errfh).
        $sel->remove($fh);
        next;
      } else { # we read data alright
        if($fh == $outfh) {
          $stdout .= $line;
        } elsif($fh == $errfh) {
          $stderr .= $line;
        }
      }
    }
  }
  print("outside while loop now\n");
  # Wait for pid to die
  waitpid $child_pid, 0;

  # Disable alarm
  alarm 0 if defined($timeout);

  if ($@) {
    $stderr = $@;
  }

  if (defined($stderr)) {
    chomp $stderr;
    if (length($stderr) > 0) {
      confess "$stderr";
    }
  }

  # If we got this far, all is good
  # Do some cleanup just in case
  $stdout =~ s/^$stdin_value// if defined($stdout);
  return $stdout;
}

my $connect_code = "conn = Sql.newInstance('jdbc:oracle:thin:" ."\@upp1" .":1521:xe', connectionProps, 'oracle.jdbc.driver.OracleDriver');";
my $sql_filename = (($^O =~ /Win32/)?'D:':'/home/jainy' ) .'/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/oracle_no_tde_no_part/db_create.sql';
#my $sql_filename = (($^O =~ /Win32/)?'D:':'/home/jainy' ) .'/YJ/Temp/smaple.txt';
#print($connect_code . "\n");
  my $script = <<EOS;
    import groovy.sql.Sql;
    import java.sql.SQLException;

    exitCode = 0;

    connectionProps = new Properties()
    connectionProps['user'] = 'aci'
    connectionProps['password'] = 'aci';

    try {
      file = new File('${sql_filename}');
      try {

        $connect_code

        try {
          file.text.replaceAll('--.*','')
            .split('[^*]/ *\\n')
            .collect({it.trim()})
            .each{ chunk ->
                   if (chunk =~ /(?i)^(create or replace procedure|begin)/) {
                     println('YJ1: ' + chunk);
                     conn.execute(chunk);
                   }
                   else {
                     chunk.split('; *\\n')
                       .collect({it.trim().replaceAll(/(?m)-\\s*\$/,'')})
                       .collect({it.trim().replaceAll(';\$','')})
                       .findAll({it && !(it =~ /(?i)^(set (pagesize|serveroutput|sqlcompat)|quit)/)})
                       .each {
                         try {
                           matcher = (it =~ /(?ms)(\\w+)\\s*(.*)/)
                           if (matcher.matches() && (matcher[0][1] ==~ /(?i)select/)) {
                             println('YJ2: ' + it);
                             conn.eachRow(it) {}{}
                           }
                           else if (matcher.matches() && (matcher[0][1] ==~ /(?i)exec/)) {
                             println('YJ3: ' + 'call ' + matcher[0][2]);
                             conn.execute('call ' + matcher[0][2]);
                           }
                           else if (matcher.matches() && (matcher[0][1] ==~ /(?i)prompt/)) {
                             println ('YJ4: ' + matcher[0][2]);
                             println matcher[0][2];
                           }
                           else {
                             println ('YJ5: ' + it);
                             conn.execute(it)
                           }
                         }
                         catch (SQLException e) {
                           if (!(e.getErrorCode() in [])) {
                             throw e;
                           }
                         }
                       }
                   }
            }
        }
        catch(e) {
          System.err.println('Error executing SQL file: ' + e);
          exitCode = 1;
        }
        finally {
          conn.close();
        }
      }
      catch(e) {
        System.err.println('Error connecting to database url: ' + e);
        exitCode = 1;
      }
    }
    catch(e) {
      System.err.println('Error opening SQL file[${sql_filename}]: ' + e);
      exitCode = 1;
    }
    System.exit(exitCode);
EOS

#print($script . "\n");
#my $ simpleScript = <<EOS;
#"exitCode =0 ;
#System.out.println('Hello World');
#"
#EOS
#my $testchomp;
#chomp($testchomp = $simpleScript);
#print($testchomp. " : done");
#chomp $simpleScript;

my $filename = 'groovy_script.txt';
confess "Could not open file '$filename' $!" unless open(my $fh, '>', $filename) ;
if (-e $filename) {
    print $fh "";
}
print $fh $script;
close $fh;
my $win_java_cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java -Xmx128m -cp C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar;C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar com.distra.pm.mcas.platform.PlatformToEnv C:/PROGRA~1/UPP/Switch/install/2.5-SNAPSHOT/instances/switch1/output/switch1/PlatformDefinition.txt';
my $linux_java_cmd = 'java -cp /home/jainy/YJ/UPP/UPP3.2/WindowsSupport/UPP-2.5-SNAPSHOT-installer/src/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar:/home/jainy/YJ/Softwares/Oracle/instantclient_11_2/ojdbc5.jar groovy.lang.GroovyShell -e "' . $script . '"';
my $java_cmd = ($^O =~ /Win32/) ? $win_java_cmd : $linux_java_cmd;
#$java_cmd =  $java_cmd. $filename;
#print ($java_cmd);
#my $grep_cmd = "grep -c \"^# User definitions\"". (($^O =~ /Win32/) ?
#                                                     (" D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/etc/distra.conf")
#                                                      :(" ~/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/etc/distra.conf"));
my ($pid, $to_chld, $fr_chld, $fr_chld_err) =
    _open3($java_cmd);

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

close($to_chld);

while ($in_sel->count() + $out_sel->count()) {
    my ($ins, $outs) = IO::Select::select($in_sel, $out_sel, undef);

    for my $fh (@$ins) {
        my $obj = $objs{ fileno($fh) };
        our $buf; local *buf = \( $obj->{buf} );
        my $bytes_read = sysread($fh, $buf, 64*1024, length($buf));
        if (!$bytes_read) {
            warn("Error reading from child: $!\n")
                if !defined($bytes_read) && !$!{ECONNRESET};
            $in_sel->remove($fh);
        }
    }

    for my $fh (@$outs) {
    }
}

my $ret = waitpid($pid, 0);
my $errno = $?;
print("waitpid returned: ${ret}\n");
print("errno: ${errno}\n");
my $stdout = $objs{ fileno( $fr_chld) }{buf};
my $stderr = $objs{ fileno( $fr_chld_err ) }{buf};
if($^O =~ /Win32/){
    $stdout =~ s/\r//g;
    $stderr =~ s/\r//g;
}

print("STDOUT:\n$stdout");
#print("\n");
print("STDERR:\n$stderr");
