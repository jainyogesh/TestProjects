#! perl

use warnings;
use strict;

use Win32::OLE qw(in);

sub matching_processes {
  my($pattern) = @_;

  my $objWMI = Win32::OLE->GetObject('winmgmts://./root/cimv2');
  my $procs = $objWMI->InstancesOf('Win32_Process');

  my @hits;
  foreach my $p (in $procs) {
    print $p."\n";
    push @hits => [ $p->Name, $p->ProcessID ]
    if $p->Name =~ /$pattern/;
  }

  1 ? @hits : \@hits;
}

print $_->[0], "\n" for matching_processes qr/^/;