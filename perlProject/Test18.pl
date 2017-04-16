#!/usr/bin/perl -w
use strict;

use Env;
use Config;
#use if ($^O =~ /Win32/), 'Win32::Process';
BEGIN {
    if($^O =~ /Win32/)
    {
        require Win32::Process;
        import Win32::Process;
        import Win32::Process::STILL_ACTIVE;
    }
}

sub my_print{
    local $_;
    my $arg = shift;
    print("$arg \n");
}

my %CHILD_PROC_OBJ = ();

if(fork()==0) {
    while (1) {
        for (keys %CHILD_PROC_OBJ) {
        print("Going to sleep\n");
        sleep 10;
        print("Wokeup!!\n");
      my $proc_obj = $CHILD_PROC_OBJ{$_};
      my $exitcode;
      $proc_obj->GetExitCode($exitcode);
      #print($exitcode."\n");
      if (Win32::Process::STILL_ACTIVE() ne $exitcode) {
        #Child is stoppped, lets exit
        print("Child exited with exit code:".$exitcode."\n");
        exit 1;
      }
      
    }
    }
    
	  
	}



my $testprog_cmd = 'C:/PROGRA~1/Java/JDK17~1.0_6/bin/java';
my $testprog_cwd = 'D:\YJ\Temp\exitCodeTest';
my $testprog_args = 'ExitCodeTest';

my $cpuload_cmd = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/executive/bin/cpuload';
my $cpuload_cwd = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/instances/switch1';
my $cpuload_args = './cpuload 2';


my $logrot_cmd = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/bin/logrotate';
my $logrotcwd = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/instances/switch1';
my $logrot_args = '" --install-type=switch --log-directory=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\log "';
my $logrot_env = '"PATH=D:\YJ\Softwares\Perl64\site\bin;D:\YJ\Softwares\Perl64\bin;C:\Program Files\RSA SecurID Token Common;.;C:\Program Files\Java\jdk1.7.0_67\bin;D:\YJ\Softwares\apache\maven\3.1.1\bin;C:\windows\system32;C:\windows;C:\windows\System32\Wbem;C:\windows\System32\WindowsPowerShell\v1.0\;C:\Program Files\TortoiseSVN\bin;C:\Program Files (x86)\Common Files\Check Point\UIFramework 3.0\Bin\;C:\Program Files (x86)\CheckPoint\Endpoint Security\Endpoint Common\bin;D:\YJ\Softwares\scripts;D:\YJ\Softwares\Oracle\instantclient_11_2 TZ=Australia/Sydney"';

my $switch_cmd = 'C:/PROGRA~1/Java/JDK16~1.0_4/bin/java';
my $switch_cwd = 'D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/instances/switch1';
#Removed from args - -Dcom.distra.pm.sysinfo.file=./cpuload -Dcom.distra.pm.heartbeat=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/instances/switch1/heartbeat.file 
my $switch_args = '-server -showversion -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xms2048m -Xmx2048m -XX:+DisableExplicitGC -XX:NewSize=100m -XX:MaxNewSize=100m -XX:MaxPermSize=128m -XX:SurvivorRatio=2 -XX:MaxTenuringThreshold=16 -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseParNewGC -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/boot-2.5-SNAPSHOT.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/annotations-1.3.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/antlr-2.7.7.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/antlr-3.3.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/antlr-runtime-3.3.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/aopalliance-1.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/asm-3.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Audit-BSI-Helper-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Audit-BSI-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Audit-BSI-Spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BasicEvaluator-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSF-AppAudit-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSF-AppAudit-Spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSF-ChangeSummary-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSF-ChangeSummary-Spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/bsf-common-svc-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/bsh-2.0b1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Common-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Core-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Core-Runtime-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Core-Spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Messaging-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Messaging-Impl-JMS-Adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Messaging-Impl-MQ-Adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Messaging-Impl-Netty-Adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSI-Messaging-Spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSIInfrastructureCommunicationImpl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/BSIInfrastructureCommunication_V1-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/common-admin-fw-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-beanutils-1.6.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-beanutils-core-1.8.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-codec-1.3.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-codec-1.6.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-collections-3.2.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-configuration-1.6.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-dbcp-1.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-digester-1.4.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-el-1.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-httpclient-3.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-io-1.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-lang-2.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-logging-1.0.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-logging-api-1.0.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-modeler-2.0.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-net-2.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/commons-pool-1.6.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Core-BSI-Support-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-cache-ehcache-adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-cache-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-cache-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-crypto-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-crypto-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-entitlements-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-entitlements-impl-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-entitlements-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-entitlements-spec-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-entitlementsauthn-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-entitlementsauthz-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-event-common-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-event-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-event-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-fieldLevelRestrictions-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-fw-bsi-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-fw-common-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-fw-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-fw-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-i18n-en-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-keymanager-cipher-adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-keymanager-keystore-adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-localization-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-localization-bsi-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-localization-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-localization-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-management-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-management-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-rules-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-rules-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-scheduler-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Core-Scoping-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Core-Scoping-Spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-sessionmanagement-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-slf4j-adf-log4j-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-testbundles-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-transformation-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-useraudit-basic-adapter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-useraudit-bsi-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-useraudit-bsi-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-useraudit-impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/core-useraudit-spec-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/dom4j-1.6.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/drools-compiler-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/drools-core-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/drools-decisiontables-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/drools-jsr94-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/drools-templates-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/ecj-3.5.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/ehcache-2.7.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/geronimo-j2ee-management_1.1_spec-1.0.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/geronimo-jms_1.1_spec-1.1.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/geronimo-jpa_2.0_spec-1.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/geronimo-jta_1.1_spec-1.1.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/groovy-all-1.8.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jackson-core-asl-1.9.12.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jackson-mapper-asl-1.9.12.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jasper-compiler-2.0.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jasper-runtime-2.0.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jasypt-1.7.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jcl-over-slf4j-1.7.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jdom-1.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jibx-extras-1.2.3.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jibx-run-1.2.3.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/joda-time-1.6.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jsr305-1.3.9.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jsr94-1.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/jxl-2.6.10.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/knowledge-api-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/knowledge-internal-api-5.5.0.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/log4j-1.2.17.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/mvel2-2.1.3.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/MVELEvaluator-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/netty-3.6.6.Final.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/openjpa-2.2.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/org.osgi.core-4.1.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/oro-2.0.8.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/Password-Encrypter-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/protobuf-java-2.3.0.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/protostuff-api-1.0.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/protostuff-core-1.0.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/protostuff-json-1.0.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/protostuff-xml-1.0.4.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/securedelete-01.10.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/serp-1.13.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/ServiceRegistry-Common-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/ServiceRegistry-Static-Impl-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/ServiceRegistryCommunication_V1-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/ServiceRegistry_V1-1.3.1.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/servlet-api-2.5.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/slf4j-api-1.7.2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-aop-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-asm-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-aspects-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-beans-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-context-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-context-support-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-core-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-expression-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-jdbc-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-jms-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-orm-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-tx-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-web-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/spring-webmvc-3.1.1.RELEASE.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/stax-api-1.0-2.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/stringtemplate-3.2.1.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/validation-api-1.0.0.GA.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/xpp3-1.1.3.4.O.jar;D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/apsf/xpp3_min-1.1.4c.jar;D:\YJ\Softwares\Oracle\instantclient_11_2/ojdbc5.jar -Djava.ext.dirs=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/lib/java/ext;C:/PROGRA~1/Java/JDK16~1.0_4/lib/ext;C:/PROGRA~1/Java/JDK16~1.0_4/jre/lib/ext -Doption.mcas.stathistory.use-temp-table=true -XX:-OmitStackTraceInFastThrow -Doptions.Xmx=2048 -Dcom.distra.pm.option.useLogFile=no -Dcom.distra.eft.device.persistent.TerminalService.block=true -Doption.mcas.db.stream.multi-updates.enable=true -Dgtrid=100 -Dcom.distra.pm.trace=off -Dmonitor=ct -DfastISB=true -Dbqual=100 -DdbsInstId=1 -Dcom.distra.eft.ep.EpNode.DO_RECOVER=true -Ddiag.mcas.group.named.vc=1 -Ddiag.mcas.group.process.vc=2 -Ddiag.mcas.service.*=4 -Ddiag.mcas.monitor.cc=2 -Ddiag.mcas.monitor.db=2 -Doption.mcas.telnet.interface= -Doption.mcas.db.schema.validate.length=1 -Doption.mcas.mgt.input-validation=1 -Dcom.distra.pm.option.logDir=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\log/switch1 -Dmcas.execproc.bin.dir=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/customerUPPbin -DNO_PARTITION=true -DTDE_DISABLED=true -Dcom.distra.pm.option.mgmtRegistryCommsSSL=TRUE -Dcom.distra.pm.option.pingCount=20 -DSWITCH_LOG_BATCH_SIZE=50 -DSWITCH_LOG_TIMEOUT=10 -DDUPLICATE_CHECK_LOG_BATCH_SIZE=50 -DDUPLICATE_CHECK_LOG_TIMEOUT=10 -Doption.mcas.switch.test.use-created=true -Dcc.scheduler.hwm=10000 -Dcc.scheduler.lwm=5000 -Dcc.log.hwm=5000 -Dcc.log.lwm=3000 -Doption.mcas.db.mon.write.cc.hwm=10 -Doption.mcas.db.mon.write.cc.lwm=0 -Dcc.sessions.hwm=10000 -Dcc.sessions.lwm=8000 -Doption.mcas.channel.msglimit=10000 -Doption.mcas.channel.test.nocc.node=*-ISS* -DNO_REPORTING_DATA=1 -DMDS_MAX_SAF_SESSION=100 -DMDS_MAX_SAF_RATE=100 -DCIS_MAX_SAF_SESSION=100 -DCIS_MAX_SAF_RATE=100 -Dcom.distra.pm.secrets.file=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/secrets -Djavax.net.ssl.keyStore=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/keystore -Djavax.net.ssl.trustStore=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/truststore -Dmcas.util.net.ssl.CRL=D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/security/ssl/crl com.distra.pm.boot.RunURL -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/classes -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/DSXLApplicationLib-V05R03140312.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/executive-2.5-SNAPSHOT.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/groovy-all-1.8.2.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/log4j-1.2.9.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/pm-2.5-SNAPSHOT.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/snmp4j-1.6d.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/mcas-2.5-SNAPSHOT/useful-2.5-SNAPSHOT.jar -cp D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/var/log4j com.distra.pm.mcas.AppServer --process=switch1 --config=. --platform=. --database=.';
my $switch_env = 'ORACLE_HOME=D:\YJ\Softwares\Oracle\instantclient_11_2 LD_LIBRARY_PATH=D:\YJ\Softwares\Oracle\instantclient_11_2;D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/executive/lib LIBPATH=D:\YJ\Softwares\Oracle\instantclient_11_2;D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/executive/lib TZ=Australia/Sydney TNS_ADMIN=';
my $switch_output = '| D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\Switch/install/2.5-SNAPSHOT/executive/bin/rollover.pl D:\YJ\UPP\UPP3.2\WindowsSupport\UPP\log/switch1/log%Y%M%D.txt \'Paused thread:\' 3 5';

my $cmd = $testprog_cmd;
my $cwd = $testprog_cwd;
my $args = $testprog_args;
#my $env = $switch_env;
my $outputFile = ' > D:\YJ\Temp\exitCodeTest\output.txt';
#setEnvironment($env);
chdir($cwd) or die "chdir failed";
my @argsArray = processArgs($args);


#Check if the command contains real file
if (! -e $cmd) {
    #Command does not contain real file. See if it just missing the extension
    my $pathext = $ENV{'PATHEXT'};
    my @extensions = split($Config{path_sep}, $pathext);
    while (@extensions) {
        my $extension = shift @extensions;
        if (-e $cmd.$extension) {
            $cmd .= $extension;
            last;
        }
    }
}

#Command should contain real file by now. We need to find the executable module now
my $execModule;
my $cmdLine;
if (-e $cmd) {
    #Get the extension by match a dot, followed by any number of non-dots until the end of the line.
    my ($extension) = $cmd =~ /(\.[^.]+)$/;
    print ("Extension:$extension\n");
    #If extension is already .exe , we are good. No need to find the executable module
    if (lc $extension ne ".exe") {
        my $fileAssoc = "assoc $extension";
        my  $defaultProgram = `for /F "tokens=2 delims==" %a in ('$fileAssoc') do \@ftype %a`;
        if ($defaultProgram =~ /=(.*exe)\s/) {
            #Got the executable
            $execModule = $1;
            #In this case commandline should consists of both execModule & $cmd
            $cmdLine = $execModule .' '.$cmd;
        }else{
            #Seems like it is a batch file, so let's find command prompt
            $execModule = `where cmd.exe`;
            #remove \r
            $execModule=~ s/\r|\n//g;
            #prepend /C to $cmd
            $cmd = '/C '.$cmd;
            #In this case commandline should only consist of $cmd
            $cmdLine = $cmd;
        }
    }else{
        $execModule = $cmd;
        #In this case commandline should only consist of $cmd
        $cmdLine = $cmd;
    }
}

die "No executable module found for $cmd" unless defined $execModule;

#Let's construct the command line now.

#if ($cmd !~ /.exe/) {
#    my_print("Inside wrong");
#    $cmdLine = $execModule .' '.$cmd;
#}else{
#    my_print("Inside correct");
#    $cmdLine = $cmd;
#}

#my $cmdLine = $execModule .' '.$cmd;
while (@argsArray) {
    my $arg = shift @argsArray;
    $cmdLine .= " ".$arg;
}
#$execModule = $ENV{'COMSPEC'};
#$cmdLine = '/C perl -e "'.$cmdLine;
#$cmdLine .= '" > D:\YJ\Temp\Output.log';
if($^O =~ /Win32/){
    print ("ExecModule:$execModule\n");
    print ("CommandLine:$cmdLine\n");
    my $ProcessObj;
    open(my $STDOUTORIG, '>&', STDOUT) or die "Failed copying STDOUT";
    open(my $STDERRORIG, '>&', STDERR) or die "Failed copying STDERR";
    open STDOUT, $outputFile or die "Failed in opening outputFile";
    open STDERR, '>&STDOUT';
    #my $flags = "Win32::Process::CREATE_NO_WINDOW,Win32::Process::NORMAL_PRIORITY_CLASS()";
    my $output = Win32::Process::Create($ProcessObj,
                                $execModule,
                                $cmdLine,
                                0,
                                 Win32::Process::NORMAL_PRIORITY_CLASS(),
                                  
                                $cwd)|| die "Failed creating a process:$!\n";
    
    my $child_pid = $ProcessObj->GetProcessID();
    $CHILD_PROC_OBJ{$child_pid} = $ProcessObj;
    print STDOUT "PID=$child_pid\n";
    open(STDOUT, '>&', $STDOUTORIG) or die "Failed restoring STDOUT";
    open(STDERR, '>&', $STDERRORIG) or die "Failed restoring STDERR";
    print($output."\n");
    print("PID:$child_pid\n");
}

while (1) {
    #print("I am running:");
    
    #for (keys %CHILD_PROC_OBJ) {
    #    print("Going to sleep\n");
    #    sleep 10;
    #    print("Wokeup!!\n");
    #  my $proc_obj = $CHILD_PROC_OBJ{$_};
    #  my $exitcode;
    #  $proc_obj->GetExitCode($exitcode);
    #  #print($exitcode."\n");
    #  if (Win32::Process::STILL_ACTIVE() ne $exitcode) {
    #    #Child is stoppped, lets exit
    #    print("Child exited with exit code:".$exitcode."\n");
    #    my $exitCode_afterproc = $? & 0xff ? -1 * ($? & 0xff) : ($? >> 8);
    #    print("Exit Code after processing:".$exitCode_afterproc."\n");
    #    exit 1;
    #  }
    #  
    #}
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

sub isProcessRunning{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $command = 'tasklist /FI "PID eq '. $pid. '"';
    #print $command."\n";
    return (`$command` =~ /$pid/) ? 1 : 0;
   }else{
    return kill 0 ,$pid;
   }
}

sub setEnvironment
{
  my $env = shift;
  my @vars;
  if($^O !~ /Win32/){
    %ENV = ();
    @vars = processArgs($env);
  }else{
    @vars = processEnvVars($env);
  }
  foreach (@vars) {
      /^([^=].*)=(.*)$/ or do { my_print("Trying to set environment with '$_'"); next; };
      my_print("Setting $_");
      $ENV{$1} = $2;
  }
}

sub processEnvVars{
    local $_;
    my @envVars = split(/([^ ]*=)/,shift);
    my @envArray;
    my $count = 0;
    while($count < @envVars) {
        my $envVarKey = $envVars[$count];
        #increment count as we defintiely got the key
        $count++;
        my $envVarVal = $envVars[$count];
        #It is possible that it is last varaible and does not has value
        $envVarVal = '' unless defined $envVarVal;
        #Check that we infact got the value and not the key again as some Env Variables can be empty.
        #If we got the value, increment the count else reset the value to empty String
        if($envVarVal !~ /=/){
            $count++;
        }else{
            $envVarVal = '';
        }
        
        $envVarKey =~ s/^\s+|\s+$//g;
        $envVarVal =~ s/^\s+|\s+$//g;
        my $envVar = $envVarKey.$envVarVal;
        if ($envVar ne "") {
            push @envArray, $envVar;
        }
    }
    return @envArray;
}

