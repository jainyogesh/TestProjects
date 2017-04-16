#!/usr/bin/perl

use strict; 
use warnings;
use File::Find;
use Carp qw (confess croak );


my @BasicModules = ('Audit-BSI-Helper', 'BSI-Messaging-Impl-MQ-Adapter', 'core-slf4j-adf-event', 'core-slf4j-adf-log4j', 'core-cache-ehcache-adapter', 'Basic-Adapters', 'core-cache-impl', 'core-event-common-impl', 'core-event-lite-impl', 'core-fw-basic-impl', 'core-fw-common-impl', 'core-management-impl', 'Basic-Impl', 'Audit-BSI-Spec', 'AuditInquiry-BSI-Spec', 'BSI-Core-Spec', 'BSI-Messaging-Spec', 'certificate-spec', 'common-admin-fw-spec', 'Core-AccessControl-Spec', 'core-cache-spec', 'Core-Entitlements-Intg-Spec', 'core-entitlements-spec', 'core-entitlements-spec-bsi', 'core-entitlementsauthn-bsi', 'core-entitlementsauthz-bsi', 'core-entitlementsprovision-bsi', 'core-event-spec', 'core-fieldLevelRestrictions-spec', 'core-fw-spec', 'core-localization-bsi', 'core-localization-spec', 'core-management-spec', 'core-orgimport-bsi', 'core-pim-spec', 'core-runtimedataexport-bsi', 'Core-Scoping-Spec', 'core-useraudit-bsi', 'core-useraudit-spec', 'core-userimport-bsi', 'Basic-Spec', 'Topology-Service-Spec', 'BasicRuntime', 'BasicRuntime-Core', 'BSI-Common', 'core-testbundles', 'Basic', );
my @SSIModules = ('IScale-Admin-BSI-Client', 'IScale-Admin-BSI-Provider', 'IScale-Admin-BSI-Spec', 'IScale-BSI-Client', 'IScale-BSI-Client-Spec', 'IScale-BSI-Common', 'IScale-BSI-Provider', 'IScale-BSI-Spec', 'IScale-DAO', 'IScale-Sync-BSI-Provider', 'IScale-Sync-BSI-Spec', 'IScale', 'SSI', 'core-cache-ehcache-adapter-listener', 'core-cache-adapters', 'core-keymanager-cipher-adapter', 'core-integration-si-adapter', 'core-useraudit-basic-adapter', 'core-velocity-template-adapter', 'core-keymanager-keystore-adapter', 'core-keymanager-adapters', 'ACIJMXBsiRemoteAdapter', 'APSFBsiRemoteAdapter', 'BSIAgentAdapter', 'ManageabilityAdapterService', 'ManageabilityService', 'core-manageability-adapters', 'MVELEvaluator', 'core-orchestration-adapters', 'core-orchestration-workflow-engine-adapter', 'SSI-Adapters', 'core-reporting-jasper-adapter', 'core-reporting-adapters', 'core-scheduler-quartz-adapter', 'core-scheduler-adapters', 'Audit-BSI-Support', 'AuditInquiry-BSI-Support', 'BSF-AppAudit-Impl', 'bsf-buspolicy-impl', 'BSF-ChangeSummary-Impl', 'bsf-common-svc-impl', 'bsf-corrgen-svc-impl', 'bsf-dualcontrol-adapter', 'BSF-DualControl-BusinessProcess', 'BSF-DualControl-SW-BusinessProcess', 'bsf-fw', 'bsf-sdm-impl', 'certificate-impl', 'certificate-support', 'common-admin-fw-uninstall-impl', 'common-utils', 'Core-AccessControl-Impl', 'core-cache-impl-interceptor', 'core-crypto-impl', 'core-entitlements-app-support', 'Core-Entitlements-BSI-Wrapper-Impl', 'core-entitlements-impl-bsi', 'core-entitlements-interceptor', 'Core-Entitlements-Intg-Impl', 'core-entitlementsauthn-bsi-support', 'core-entitlementsauthz-bsi-support', 'core-entitlementsprovision-bsi-support', 'core-etm-impl', 'core-event-consumer-impl', 'core-event-impl', 'core-fw-impl', 'core-fw-utils-impl', 'core-housekeeping-bsi', 'core-housekeeping-bsi-impl', 'core-housekeeping-impl', 'core-integration-impl', 'core-iscale-impl', 'core-iscale-support', 'core-localization-bsi-impl', 'core-localization-bsi-support', 'core-localization-bundle-impl', 'core-localization-common-impl', 'core-localization-db-impl', 'core-logging-event-consumer-impl', 'core-orchestration-impl', 'core-orgimport-bsi-support', 'core-referenceData-impl', 'core-reporting-impl', 'core-reportscheduler-impl', 'core-roleimport-bsi-support', 'core-rules-impl', 'core-runtimedataexport-bsi-support', 'core-scheduler-impl', 'Core-Scoping-Impl', 'core-sessionmanagement-bsi', 'core-useraudit-bsi-support', 'core-useraudit-impl', 'core-userimport-bsi-support', 'core-workflow-engine-impl', 'core-workflow-engine-iscale-impl', 'Dynamic-CRUD-Service-Impl', 'IScale-SyncMonitor-Impl', 'core-management-bsi-metricdataproviderservice', 'core-management-bsi-resourcemongroupinfoservice', 'core-management-db-service-bsi-crud', 'core-management-db-service-crud', 'core-management-db-service-impl', 'Manageability-Seed-Data', 'Manageability', 'SSI-Impl', 'Topology', 'Topology-Service-Impl', 'BSF-AppAudit-Spec', 'bsf-buspolicy-spec', 'BSF-ChangeSummary-Spec', 'bsf-common-svc-spec', 'bsf-corrgen-svc-spec', 'bsf-sdm-spec', 'core-crypto-spec', 'core-etm-spec', 'core-event-consumer-spec', 'core-housekeeping-spec', 'core-iscale-spec', 'core-mail-spec', 'core-management-db-service-spec', 'core-message-spec', 'core-orchestration-spec', 'core-referenceData-spec', 'core-reporting-spec', 'core-reportscheduler-spec', 'core-rules-spec', 'core-scheduler-spec', 'core-templates-spec', 'core-workflow-engine-spec', 'IScale-SyncMonitor-Spec', 'SSI-Spec', 'SSIRuntime', 'SSICoreRuntime', 'SSIDataAccessRuntime', 'SSIEntitlementsAppSupportRuntime', ); 
my @UIIModules = ('UII', 'core-sso-basic-entitlements-adapter', 'UII-Adapters', 'EMF-Core-DataAccess', 'EMF-Core-Impl', 'EMF-Core-Spec', 'EMF-Core', 'EMF-CRUD-Service-Impl', 'EMF-CRUD', 'AppEnvMgmt', 'CommonDataTypes', 'DBConnMgmt', 'DMDnld', 'EMF-UI-Common', 'EntityMgmt', 'MenuMgmt', 'PageMgmt', 'EMF-Design-UI', 'ReportAssemblyMgmt', 'EMF-DM-Common', 'EMF-Packaging-Impl', 'EMF-Packaging-Tool', 'EMF-Report-Jasper-Adapter', 'EMF-Report-Service-Impl', 'EMF-Report-Service-Spec', 'EMF-Report-WebApp', 'EMF-Report', 'EMF-Runtime-UI', 'Report-Center-UI', 'EMF-WebApp', 'UII-EMF', 'bsf-admin-urlvalidation-svc-impl', 'bsf-pim-common-impl', 'BSF-SavedSearch-Impl', 'bsf-urlvalidation-svc-impl', 'core-fieldLevelRestrictions-impl', 'core-sso-client-impl', 'core-sso-common-impl', 'core-sso-provider-impl', 'core-sso-saml-common-impl', 'EmbeddedHelp-BSI-Impl-Mock', 'UII-Impl', 'bsf-admin-urlvalidation-svc-spec', 'bsf-urlvalidation-svc-spec', 'core-sso-spec', 'EmbeddedHelp-BSI-Spec', 'UII-Spec', 'bsf-webapp', 'core-webapp', 'core-webapp-automation', 'dashboard-plugin', 'DashboardProj', 'dashboard', 'UII-Web', 'responsive-webapp', 'UIIRuntime', 'UIICoreRuntime', );
my @BSIModules = ('BSI-Messaging-Impl-JMS-Adapter', 'BSI-Messaging-Impl-Netty-Adapter', 'BSI-Adapters', 'BSI-Core-Impl', 'BSI-Core-Runtime-Impl', 'BSI-Enhanced-Messaging-Impl', 'BSI-Messaging-Impl', 'BSIInfrastructureCommunication_V1', 'BSIInfrastructureCommunicationImpl', 'Core-BSI-Support', 'core-entitlements-support', 'core-entitlements-support-bsi', 'core-fw-bsi-impl', 'BSI-Impl', 'ServiceRegistry_V1', 'ServiceRegistry-Common-Impl', 'ServiceRegistry-Static-Impl', 'ServiceRegistryCommunication_V1', 'BSI-Core-Runtime-Spec', 'BSI-Spec', 'bsi-test-driver', 'BSIRuntime', 'BSI', );
my @EntitlementsModules = ('BasicEvaluator', 'BasicPasswordAuthNProvider', 'Digital-Signature-Autouser-Impl', 'DigitalSignatureAuthNProvider', 'JaasPamAuthNProvider', 'LDAPAuthNProvider', 'LDAPUserSyncSourceProvider', 'Entitlements-Adapters', 'PropertyBasedAuthNProvider', 'UserSynchronizationService', 'Audit-BSI-Impl', 'AuditInquiry-BSI-Impl', 'core-entitlements-certificate-expiry-impl', 'core-entitlements-certificate-expiry-scheduler-impl', 'core-entitlements-impl', 'core-entitlementsauthn-bsi-impl', 'core-entitlementsauthz-bsi-impl', 'core-entitlementsprovision-bsi-impl', 'core-orgimport-bsi-impl', 'core-roleimport-bsi-impl', 'core-runtimedataexport-bsi-impl', 'core-sessionmanagement-bsi-impl', 'core-useraudit-bsi-impl', 'core-userimport-bsi-impl', 'core-userimport-scheduler-impl', 'entitlements-consumer-helper', 'Entitlements-Impl', 'Entitlements-Spec', 'EntitlementsRuntime', 'EntitlementsParent', );
my @TokenizationModules = ('tokenization', 'tokenization-adf-seed-data', 'tokenization-bsi-common', 'tokenization-bsi-consumer', 'tokenization-bsi-provider', 'tokenization-db-schema', 'tokenization-server-app', 'tokenization-service-impl', 'tokenization-service-multi-site-async-impl', 'tokenization-service-multi-site-impl', 'tokenization-service-spec', 'TokenType-Bsi-Consumer', 'TokenType-Bsi-Provider', 'TokenType-Bsi-Spec', );
my @InfrastrAdmSvcModules = ('InfrastrAdmSvc-Adapters', 'AppConfigAdminUI', 'CommonUI', 'core-entitlements-install-impl', 'EmbeddedHelpContent', 'EntitlementsAdminUI', 'EventManagementAdminUI', 'HousekeepingUI', 'iscale-ent-mgmt-seed-data', 'KekManagementUI', 'KeyManagementUI', 'manageabilityadmin-ui', 'InfrastrAdmSvc-Impl', 'ServiceRegistryUI', 'StaticRefDataAdminUI', 'TokenTypeUI', 'InfrastrAdmSvc-Spec', 'InfrastrAdmSvcRuntime', 'InfrastrAdmSvc', );



my $root = 'C:/YJ/Codebase/APSF-141Drop2/Modules/';
my @rootModules = ('Basic', 'SSI', 'UII', 'Entitlements', 'Tokenization', 'InfrastrAdmSvc');

foreach my $module (@rootModules){
  my $rootDir = $root + '/' +$module;
  
  foreach my $replaceModule (@rootModules){
    if ($module != $replaceModule) {
      
    }
    
  }
  
}


find({ wanted => \&process_file}, $root);

sub process_file {
  if ($File::Find::name =~ /pom.xml/){
    my $filepath = $File::Find::name;
    my $text;
    open FILE, $filepath or die "Couldn't open file: $!"; 
    while (<FILE>){
     $text .= $_;
    }
    close FILE;
    
    foreach my $artifact (@uiiModules) { 
      $text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>\s*\n*\s*<version>)(\${project.parent.version}<\/version>)/$1\${uii.version}<\/version>/gs;
      $text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>\s*\n*\s*<version>)(\${apsf.version}<\/version>)/$1\${uii.version}<\/version>/gs;
      $text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>\s*\n*\s*<version>)(\${project.version}<\/version>)/$1\${uii.version}<\/version>/gs;
    }
    
    open (WRITEFILE, ">$filepath") or die "Couldn't open file: $!"; 
    print WRITEFILE $text;
    close WRITEFILE;
  }
}
#
#my $filepath = 'C:/YJ/Codebase/APSF-141Drop2/Modules/SSI/iScale/IScale-Admin-BSI-Client/pom.xml';
#my $text;
#open FILE, $filepath or die "Couldn't open file: $!"; 
#while (<FILE>){
# $text .= $_;
#}
#close FILE;
#
##print ($string);
##my $expression = 's/(<dependency>(.+?)<artifactId>core-fw-impl<\/artifactId>(.+?))(<version>.*<\/version>)/$1<version>\${basic.version}<\/version>/s';
#
##file_regex_expression({filename => $filepath, expression => $expression});
#
#sub file_regex_expression{
#  # Used to execute regular expression
#  my ($arg_ref) = @_;
#  
#  confess "No filename defined" if !exists($arg_ref->{'filename'});
#  confess "No expression defined" if !exists($arg_ref->{'expression'});
#
#  my $filename = $arg_ref->{'filename'};
#  my $expression = $arg_ref->{'expression'};
#  
#  print ("$expression \n");
#  my $exitStatus;
#  if (! -e $filename) {
#    confess "The specified filename does not exist";
#  }
#  if ($^O =~ /Win/) {
#    $exitStatus = system("perl -pi.nonexistentext -e \"$expression\" $filename");
#    unlink($filename.".nonexistentext");
#  }else{
#    $exitStatus = system("perl -pi -e \"$expression\" $filename");
#  }
#  return $exitStatus;
#}
#
#
##my $text = '<dependency>
##           <groupId>${project.groupId}</groupId>
##           <artifactId>core-fw-impl</artifactId>
##           <version>${apsf.version}</version>
##           <scope>test</scope>
##       </dependency>
##		<dependency>
##           <groupId>${project.groupId}</groupId>
##           <artifactId>core-fw-impl</artifactId>
##           <version>${apsf.version}</version>
##           <type>test-jar</type>
##           <scope>test</scope>
##       </dependency>
##        <dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>core-testbundles</artifactId>
##            <version>${apsf.version}</version>
##            <scope>test</scope>
##        </dependency>
##	   
##	<!--APSF dependencies-->  
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>BasicRuntime-Core</artifactId>
##            <version>${apsf.version}</version>
##            <scope>${module.scope}</scope>
##			<type>pom</type>
##        </dependency>
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>BSIRuntime</artifactId>
##            <version>${apsf.version}</version>
##            <scope>${module.scope}</scope>
##			<type>pom</type>
##        </dependency>
##  
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>IScale-Admin-BSI-Spec</artifactId>
##            <version>${apsf.version}</version>
##            <scope>${module.scope}</scope>
##        </dependency>
##		<dependency>
##			<groupId>${project.groupId}</groupId>
##			<artifactId>core-iscale-impl</artifactId>
##			<version>${apsf.version}</version>
##			<scope>${module.scope}</scope>
##		</dependency>
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>core-fw-utils-impl</artifactId>
##            <version>${apsf.version}</version>
##            <scope>${module.scope}</scope>
##        </dependency>				
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>IScale-BSI-Client</artifactId>
##            <version>${apsf.version}</version>
##            <scope>${module.scope}</scope>
##        </dependency>
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>IScale-Admin-BSI-Provider</artifactId>
##            <version>${apsf.version}</version>
##            <scope>runtime</scope>
##        </dependency>
##		<dependency>
##            <groupId>${project.groupId}</groupId>
##            <artifactId>IScale-BSI-Common</artifactId>
##            <version>${apsf.version}</version>
##            <scope>${module.scope}</scope>
##        </dependency>';
#
##print($text."\n");
#my $artifact = 'core-fw-impl';
#
#$text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>.*?<version>)(.*?<\/version>)/$1\${basic.version}<\/version>/gs;
#
#print("After Replace\n");
#print($text."\n");
#
#open (WRITEFILE, ">$filepath") or die "Couldn't open file: $!"; 
#print WRITEFILE $text;
#close WRITEFILE;
#
#
#
#
