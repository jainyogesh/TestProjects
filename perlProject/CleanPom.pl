#!/usr/bin/perl

use strict; 
use warnings;
use File::Find;
use Carp qw (confess croak );

#my $dir = 'C:\YJ\Codebase\modular_trunk\Modules\UII\UII-Web\Responsive-WebApp';
#find({ wanted => \&process_file_replace_version}, $dir);

process_pom_post_move();


sub process_pom_post_move {
  
  my $oldParentPom = 'C:\YJ\Codebase\Modular_Trunk\Modules\Entitlements\pom.xml';
  my $oldParentArtifact = 'EntitlementsParent';
  
  my $currentPom = 'C:\YJ\Codebase\Modular_Trunk\Modules\Entitlements\Entitlements-Spec\pom.xml';
  my $currentArtifact = 'Entitlements-Spec';
  
  print 'perl -pi.nonexistant -e "s/\s*<module>Entitlements-Spec<\/module>\s*//g"' . " $oldParentPom\n";
  print "del $oldParentPom.nonexistant"
  
}

sub process_file_arrange_version {
  #if ($File::Find::dir =~ /Responsive-WebApp/) {
  #  $File::Find::prune = 1;
  #  return; 
  #}
  
  if ($File::Find::name =~ /pom.xml/){
    my $filepath = $File::Find::name;
    my $text;
    open FILE, $filepath or die "Couldn't open file: $!"; 
    while (<FILE>){
     $text .= $_;
    }
    close FILE;
    
    $text =~ s/(<version>.*<\/version>)(\s*\n*\s*)(<artifactId>.*<\/artifactId>)/$3$2$1/g;
    
    open (WRITEFILE, ">$filepath") or die "Couldn't open file: $!"; 
    print WRITEFILE $text;
    close WRITEFILE;
  }
}

sub process_file_replace_version {
  
  #if ($File::Find::dir =~ /Responsive-WebApp/) {
  #  $File::Find::prune = 1;
  #  return; 
  #}
  
  if ($File::Find::name =~ /pom.xml/){
    my $filepath = $File::Find::name;
    my $text;
    open FILE, $filepath or die "Couldn't open file: $!"; 
    while (<FILE>){
     $text .= $_;
    }
    close FILE;
    
    $text =~ s/<version>\${apsf.version}<\/version>/<version>\${project.version}<\/version>/g;
    
    open (WRITEFILE, ">$filepath") or die "Couldn't open file: $!"; 
    print WRITEFILE $text;
    close WRITEFILE;
  }
}