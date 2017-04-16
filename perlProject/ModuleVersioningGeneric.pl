#!/usr/bin/perl

use strict; 
use warnings;
use File::Find;
use Carp qw (confess croak );

my %rootSubModulesMap = (
 Basic => ['Audit-BSI-Helper', 'BSI-Messaging-Impl-MQ-Adapter', 'core-slf4j-adf-custom', 'core-slf4j-adf-event', 'core-slf4j-adf-log4j', 'core-cache-ehcache-adapter', 'Basic-Adapters', 'core-cache-impl', 'core-commandline-common', 'core-event-common-impl', 'core-event-lite-impl', 'core-fw-basic-impl', 'core-fw-common-impl', 'core-management-impl', 'Basic-Impl', 'Audit-BSI-Spec', 'AuditInquiry-BSI-Spec', 'BSI-Core-Spec', 'BSI-Messaging-Spec', 'certificate-spec', 'common-admin-fw-spec', 'Core-AccessControl-Spec', 'core-cache-spec', 'Core-Entitlements-Intg-Spec', 'core-entitlements-spec', 'core-entitlements-spec-bsi', 'core-entitlementsauthn-bsi', 'core-entitlementsauthz-bsi', 'core-entitlementsprovision-bsi', 'core-event-spec', 'core-fieldLevelRestrictions-spec', 'core-fw-spec', 'core-localization-bsi', 'core-localization-spec', 'core-management-spec', 'core-orgimport-bsi', 'core-pim-spec', 'core-runtimedataexport-bsi', 'Core-Scoping-Spec', 'core-useraudit-bsi', 'core-useraudit-spec', 'core-userimport-bsi', 'Basic-Spec', 'Topology-Service-Spec', 'BasicRuntime', 'BasicRuntime-Core', 'BSI-Common', 'core-testbundles', 'Basic', ],
 SSI => ['IScale-Admin-BSI-Client', 'IScale-Admin-BSI-Provider', 'IScale-Admin-BSI-Spec', 'IScale-BSI-Client', 'IScale-BSI-Client-Spec', 'IScale-BSI-Common', 'IScale-BSI-Provider', 'IScale-BSI-Spec', 'IScale-DAO', 'IScale-Sync-BSI-Provider', 'IScale-Sync-BSI-Spec', 'IScale', 'SSI', 'core-cache-ehcache-adapter-listener', 'core-cache-adapters', 'core-keymanager-cipher-adapter', 'core-integration-si-adapter', 'core-useraudit-basic-adapter', 'core-velocity-template-adapter', 'core-keymanager-keystore-adapter', 'core-keymanager-adapters', 'ACIJMXBsiRemoteAdapter', 'APSFBsiRemoteAdapter', 'BSIAgentAdapter', 'ManageabilityAdapterService', 'ManageabilityService', 'core-manageability-adapters', 'MVELEvaluator', 'core-orchestration-adapters', 'core-orchestration-workflow-engine-adapter', 'SSI-Adapters', 'core-reporting-jasper-adapter', 'core-reporting-adapters', 'core-scheduler-quartz-adapter', 'core-scheduler-adapters', 'Audit-BSI-Support', 'AuditInquiry-BSI-Support', 'BSF-AppAudit-Impl', 'bsf-buspolicy-impl', 'BSF-ChangeSummary-Impl', 'bsf-common-svc-impl', 'bsf-corrgen-svc-impl', 'bsf-dualcontrol-adapter', 'BSF-DualControl-BusinessProcess', 'BSF-DualControl-SW-BusinessProcess', 'bsf-fw', 'bsf-sdm-impl', 'certificate-impl', 'certificate-support', 'common-admin-fw-uninstall-impl', 'common-utils', 'Core-AccessControl-Impl', 'core-cache-impl-interceptor', 'core-crypto-impl', 'core-entitlements-app-support', 'Core-Entitlements-BSI-Wrapper-Impl', 'core-entitlements-impl-bsi', 'core-entitlements-interceptor', 'Core-Entitlements-Intg-Impl', 'core-entitlementsauthn-bsi-support', 'core-entitlementsauthz-bsi-support', 'core-entitlementsprovision-bsi-support', 'core-etm-impl', 'core-event-consumer-impl', 'core-event-impl', 'core-fw-impl', 'core-fw-utils-impl', 'core-housekeeping-bsi', 'core-housekeeping-bsi-impl', 'core-housekeeping-impl', 'core-integration-impl', 'core-iscale-impl', 'core-iscale-support', 'core-localization-bsi-impl', 'core-localization-bsi-support', 'core-localization-bundle-impl', 'core-localization-common-impl', 'core-localization-db-impl', 'core-logging-event-consumer-impl', 'core-orchestration-impl', 'core-orgimport-bsi-support', 'core-referenceData-impl', 'core-reporting-impl', 'core-reportscheduler-impl', 'core-roleimport-bsi-support', 'core-rules-impl', 'core-runtimedataexport-bsi-support', 'core-scheduler-impl', 'Core-Scoping-Impl', 'core-sessionmanagement-bsi', 'core-useraudit-bsi-support', 'core-useraudit-impl', 'core-userimport-bsi-support', 'core-workflow-engine-impl', 'core-workflow-engine-iscale-impl', 'Dynamic-CRUD-Service-Impl', 'IScale-SyncMonitor-Impl', 'core-management-bsi-metricdataproviderservice', 'core-management-bsi-resourcemongroupinfoservice', 'core-management-db-service-bsi-crud', 'core-management-db-service-crud', 'core-management-db-service-impl', 'Manageability-Seed-Data', 'Manageability', 'SSI-Impl', 'Topology', 'Topology-Service-Impl', 'BSF-AppAudit-Spec', 'bsf-buspolicy-spec', 'BSF-ChangeSummary-Spec', 'bsf-common-svc-spec', 'bsf-corrgen-svc-spec', 'bsf-sdm-spec', 'core-crypto-spec', 'core-etm-spec', 'core-event-consumer-spec', 'core-housekeeping-spec', 'core-iscale-spec', 'core-mail-spec', 'core-management-db-service-spec', 'core-message-spec', 'core-orchestration-spec', 'core-referenceData-spec', 'core-reporting-spec', 'core-reportscheduler-spec', 'core-rules-spec', 'core-scheduler-spec', 'core-templates-spec', 'core-workflow-engine-spec', 'IScale-SyncMonitor-Spec', 'SSI-Spec', 'SSIRuntime', 'SSICoreRuntime', 'SSIDataAccessRuntime', 'SSIEntitlementsAppSupportRuntime', ],
 UII => ['UII', 'core-sso-basic-entitlements-adapter', 'UII-Adapters', 'EMF-Core-DataAccess', 'EMF-Core-Impl', 'EMF-Core-Spec', 'EMF-Core', 'EMF-CRUD-Service-Impl', 'EMF-CRUD', 'AppEnvMgmt', 'CommonDataTypes', 'DBConnMgmt', 'DMDnld', 'EMF-UI-Common', 'EntityMgmt', 'MenuMgmt', 'PageMgmt', 'EMF-Design-UI', 'ReportAssemblyMgmt', 'EMF-DM-Common', 'EMF-Packaging-Impl', 'EMF-Packaging-Tool', 'EMF-Report-Jasper-Adapter', 'EMF-Report-Service-Impl', 'EMF-Report-Service-Spec', 'EMF-Report-WebApp', 'EMF-Report', 'EMF-Runtime-UI', 'Report-Center-UI', 'EMF-WebApp', 'UII-EMF', 'bsf-admin-urlvalidation-svc-impl', 'bsf-pim-common-impl', 'BSF-SavedSearch-Impl', 'bsf-urlvalidation-svc-impl', 'core-fieldLevelRestrictions-impl', 'core-sso-client-impl', 'core-sso-common-impl', 'core-sso-provider-impl', 'core-sso-saml-common-impl', 'EmbeddedHelp-BSI-Impl-Mock', 'UII-Impl', 'bsf-admin-urlvalidation-svc-spec', 'bsf-urlvalidation-svc-spec', 'core-sso-spec', 'EmbeddedHelp-BSI-Spec', 'UII-Spec', 'bsf-webapp', 'core-webapp', 'core-webapp-automation', 'dashboard-plugin', 'DashboardProj', 'dashboard', 'UII-Web', 'responsive-webapp', 'UIIRuntime', 'UIICoreRuntime', ],
 BSI => ['BSI-Messaging-Impl-JMS-Adapter', 'BSI-Messaging-Impl-Netty-Adapter', 'BSI-Adapters', 'BSI-Core-Impl', 'BSI-Core-Runtime-Impl', 'BSI-Enhanced-Messaging-Impl', 'BSI-Messaging-Impl', 'BSIInfrastructureCommunication_V1', 'BSIInfrastructureCommunicationImpl', 'Core-BSI-Support', 'core-entitlements-support', 'core-entitlements-support-bsi', 'core-fw-bsi-impl', 'BSI-Impl', 'ServiceRegistry_V1', 'ServiceRegistry-Common-Impl', 'ServiceRegistry-Static-Impl', 'ServiceRegistryCommunication_V1', 'BSI-Core-Runtime-Spec', 'BSI-Spec', 'bsi-test-driver', 'BSIRuntime', 'BSI', ],
 Entitlements => ['BasicEvaluator', 'BasicPasswordAuthNProvider', 'Digital-Signature-Autouser-Impl', 'DigitalSignatureAuthNProvider', 'JaasPamAuthNProvider', 'LDAPAuthNProvider', 'LDAPUserSyncSourceProvider', 'Entitlements-Adapters', 'PropertyBasedAuthNProvider', 'UserSynchronizationService', 'Audit-BSI-Impl', 'AuditInquiry-BSI-Impl', 'core-entitlements-certificate-expiry-impl', 'core-entitlements-certificate-expiry-scheduler-impl', 'core-entitlements-impl', 'core-entitlementsauthn-bsi-impl', 'core-entitlementsauthz-bsi-impl', 'core-entitlementsprovision-bsi-impl', 'Core-Migrator-Common', 'core-orgimport-bsi-impl', 'core-roleimport-bsi-impl', 'core-runtimedataexport-bsi-impl', 'core-sessionmanagement-bsi-impl', 'core-useraudit-bsi-impl', 'core-userimport-bsi-impl', 'core-userimport-scheduler-impl', 'entitlements-consumer-helper', 'Entitlements-Impl', 'Entitlements-Spec', 'EntitlementsRuntime', 'EntitlementsParent', ],
 Tokenization => ['tokenization', 'tokenization-adf-seed-data', 'tokenization-bsi-common', 'tokenization-bsi-consumer', 'tokenization-bsi-provider', 'tokenization-db-schema', 'tokenization-server-app', 'tokenization-service-impl', 'tokenization-service-multi-site-async-impl', 'tokenization-service-multi-site-impl', 'tokenization-service-spec', 'TokenType-Bsi-Consumer', 'TokenType-Bsi-Provider', 'TokenType-Bsi-Spec', ],
 InfrastrAdmSvc => ['InfrastrAdmSvc-Adapters', 'AppConfigAdminUI', 'CommonUI', 'core-entitlements-install-impl', 'EmbeddedHelpContent', 'EntitlementsAdminUI', 'EventManagementAdminUI', 'HousekeepingUI', 'iscale-ent-mgmt-seed-data', 'KekManagementUI', 'KeyManagementUI', 'manageabilityadmin-ui', 'InfrastrAdmSvc-Impl', 'ServiceRegistryUI', 'StaticRefDataAdminUI', 'TokenTypeUI', 'InfrastrAdmSvc-Spec', 'InfrastrAdmSvcRuntime', 'InfrastrAdmSvc', ]

);

my $root = 'C:\YJ\Codebase\Modular_Trunk\Modules';
my @rootModules = ('Basic', 'SSI', 'UII', 'BSI','Entitlements', 'Tokenization', 'InfrastrAdmSvc');
my $module = '';
foreach my $moduleDir (@rootModules){
  $module = $moduleDir;
  my $rootDir = $root .'/' .$moduleDir;
  find({ wanted => \&process_file}, $rootDir);
  
}




sub process_file {
  if ($File::Find::name =~ /pom.xml/){
    my $filepath = $File::Find::name;
    my $text;
    open FILE, $filepath or die "Couldn't open file: $!"; 
    while (<FILE>){
     $text .= $_;
    }
    close FILE;
    print "Module:". $module ."\n";
    foreach my $rootSubModule ( keys %rootSubModulesMap){
      print "ReplaceModule:" .$rootSubModule ."\n";
      my $replaceString = $rootSubModule.'.version';
      if ($module ne $rootSubModule) {
        foreach my $artifact (@{$rootSubModulesMap{$rootSubModule}}) {
          print $artifact ."\n";
          $text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>\s*\n*\s*<version>)(\${project.parent.version}<\/version>)/$1\${$replaceString}<\/version>/gsi;
          $text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>\s*\n*\s*<version>)(\${apsf.version}<\/version>)/$1\${$replaceString}<\/version>/gsi;
          $text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>\s*\n*\s*<version>)(\${project.version}<\/version>)/$1\${$replaceString}<\/version>/gsi;
        }
      }
    }
    
    open (WRITEFILE, ">$filepath") or die "Couldn't open file: $!"; 
    print WRITEFILE $text;
    close WRITEFILE;
  }
}
