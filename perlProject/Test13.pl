#!/usr/bin/perl -w
use strict;

STARTCMD: {
    print("Started script: $0 \n");
  print("Script arguments: @ARGV \n") if (@ARGV);
  sleep 10;
};