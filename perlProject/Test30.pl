#!/usr/bin/perl -w
use strict;

  my $cartridge_filename = '/home/jainy/UPP/Switch/install/2.5-SNAPSHOT/cartridges/amex-gcag-2.5-SNAPSHOT-cartridge.jar';
	#my $cartridge_filename = 'D:/YJ/UPP/UPP3.2/WindowsSupport/UPP/Switch/install/2.5-SNAPSHOT/cartridges/amex-gcag-2.5-SNAPSHOT-cartridge.jar';
  
  #Placing the same check which we have in Java code
  my $url = ($cartridge_filename =~ /^\//) ? "url=file://" . $cartridge_filename:"url=file:///" . $cartridge_filename;

  my $mcas_cmd = "mcas/config storeCartridge ".$url;
	print($mcas_cmd);

  kill 'TERM',10808;