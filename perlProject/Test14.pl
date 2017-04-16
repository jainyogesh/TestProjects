#!/usr/bin/perl -w
use strict;


my $pid = fork;

if ($pid == 0) {
    #Child
    #if ($^O =~ /Win32/) {
    #   open (STDIN, '<&NUL');
    #    open (STDOUT, '>&NUL');
    #    open STDERR, ">&STDOUT";
    #}else{
    #    open (STDIN, '</dev/null');
    #    open (STDOUT, '>/dev/null');
    #    open STDERR, ">/dev/null";
    #}
    #
    #system("java LineTest") or die("Error in exec $!");
    #while (1) {
        print("Child\n");
        sleep 10;
    #}
    
}else{
    #Parent
    print("I have started the child\n");
    exit(0);
}
