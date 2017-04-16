#!/usr/bin/perl

use strict; 
use warnings;
use File::Find;
use Carp qw (confess croak );

my $dir = 'C:/YJ/Codebase/APSF-141Drop2/Modules/SSI';

#find({ wanted => \&process_file}, $dir);

sub process_file {
print "Found $File::Find::name \n" if $File::Find::name =~ /pom.xml/;
}

#my $filepath = 'C:/YJ/Codebase/APSF-141Drop2/Modules/SSI/iScale/IScale-Admin-BSI-Client/pom.xml';
#my $text;
#open FILE, $filepath or die "Couldn't open file: $!"; 
#while (<FILE>){
# $text .= $_;
#}
#close FILE;

#print ($string);
#my $expression = 's/(<dependency>(.+?)<artifactId>core-fw-impl<\/artifactId>(.+?))(<version>.*<\/version>)/$1<version>\${basic.version}<\/version>/s';

#file_regex_expression({filename => $filepath, expression => $expression});

sub file_regex_expression{
  # Used to execute regular expression
  my ($arg_ref) = @_;
  
  confess "No filename defined" if !exists($arg_ref->{'filename'});
  confess "No expression defined" if !exists($arg_ref->{'expression'});

  my $filename = $arg_ref->{'filename'};
  my $expression = $arg_ref->{'expression'};
  
  print ("$expression \n");
  my $exitStatus;
  if (! -e $filename) {
    confess "The specified filename does not exist";
  }
  if ($^O =~ /Win/) {
    $exitStatus = system("perl -pi.nonexistentext -e \"$expression\" $filename");
    unlink($filename.".nonexistentext");
  }else{
    $exitStatus = system("perl -pi -e \"$expression\" $filename");
  }
  return $exitStatus;
}


my $text = '<dependencies>
<dependency>
           <groupId>${project.groupId}</groupId>
           <artifactId>core-fw-impl</artifactId>
           <version>${apsf.version}</version>
           <scope>test</scope>
       </dependency>
		<dependency>
           <groupId>${project.groupId}</groupId>
           <version>${apsf.version}</version>
           <artifactId>core-fw-impl</artifactId>
           <type>test-jar</type>
           <scope>test</scope>
       </dependency>
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>core-testbundles</artifactId>
            <version>${apsf.version}</version>
            <scope>test</scope>
        </dependency>
	   
	<!--APSF dependencies-->  
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>BasicRuntime-Core</artifactId>
            <version>${apsf.version}</version>
            <scope>${module.scope}</scope>
			<type>pom</type>
        </dependency>
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>BSIRuntime</artifactId>
            <version>${apsf.version}</version>
            <scope>${module.scope}</scope>
			<type>pom</type>
        </dependency>
  
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>IScale-Admin-BSI-Spec</artifactId>
            <version>${apsf.version}</version>
            <scope>${module.scope}</scope>
        </dependency>
		<dependency>
			<groupId>${project.groupId}</groupId>
			<artifactId>core-iscale-impl</artifactId>
			<version>${apsf.version}</version>
			<scope>${module.scope}</scope>
		</dependency>
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>core-fw-utils-impl</artifactId>
            <version>${apsf.version}</version>
            <scope>${module.scope}</scope>
        </dependency>				
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>IScale-BSI-Client</artifactId>
            <version>${apsf.version}</version>
            <scope>${module.scope}</scope>
        </dependency>
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>IScale-Admin-BSI-Provider</artifactId>
            <version>${apsf.version}</version>
            <scope>runtime</scope>
        </dependency>
		<dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>IScale-BSI-Common</artifactId>
            <version>${apsf.version}</version>
            <scope>${module.scope}</scope>
        </dependency>
        </dependencies>';

#print($text."\n");
my $artifact = 'core-fw-impl';

$text =~ s/(<version>.*<\/version>)(\s*\n*\s*)(<artifactId>.*<\/artifactId>)/$3$2$1/g;


#$text =~ s/(<dependency>.*?<artifactId>$artifact<\/artifactId>.*?<version>)(.*)(<\/version>)/$1\${basic.version}<\/version>/gs;
#
 #$text =~ s/<dependencies>.*?<\/dependencies>/<dependencies>\$formattedDependecies<\/dependencies>/
#if ($text =~/(<dependency>.*?<\/dependency>)/s) {
#  my $dependency = $1;
#  print ("Dependnecy $dependency  \n");
#  if ($dependency =~ /<artifactId>$artifact<\/artifactId>/) {
#    $dependency =~ s/<version>.*version.*<\/version>/<version>\${basic.version}<\/version>/g;
#  }
#  
#}

#print("After Replace\n");
print($text."\n");

#open (WRITEFILE, ">$filepath") or die "Couldn't open file: $!"; 
#print WRITEFILE $text;
#close WRITEFILE;




