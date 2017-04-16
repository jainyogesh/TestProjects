#!/usr/bin/perl -w
use strict;
BEGIN {
    if($^O =~ /Win32/)
    {
        require Win32::Process;
        import Win32::Process;
    }
}
#system(1, "wperl $INSTALL_DIR/procmgr.pl $PROC_DIR > NUL 2>&1");
my $ProcessObj;
my $execModule = 'D:\YJ\Softwares\Perl64\bin\perl.exe';
my $cmdLine = 'perl D:\YJ\Temp\Test18.pl';
open(my $STDOUTORIG, '>&', STDOUT) or die "Failed copying STDOUT";
open(my $STDERRORIG, '>&', STDERR) or die "Failed copying STDERR";
open STDOUT, 'NUL';
open STDERR, '>&STDOUT';
Win32::Process::Create($ProcessObj,
                                $execModule,
                                $cmdLine,
                                0,
                                 Win32::Process::CREATE_NO_WINDOW(),
                                  
                                ".")|| die "Failed creating a process:$!\n";
open(STDOUT, '>&', $STDOUTORIG) or die "Failed restoring STDOUT";
open(STDERR, '>&', $STDERRORIG) or die "Failed restoring STDERR";