# perl 
# See https://rt.perl.org/Ticket/Display.html?id=121975

use Devel::Peek;
$reps = shift(@ARGV);
$x = "x" x $reps;
$y = $x for 1..$reps;
Dump($x);