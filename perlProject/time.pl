use POSIX qw ( strftime );

my $time = strftime("%Y-%m-%d %H:%M:%S %Z", localtime(time));
print $time;