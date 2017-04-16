BEGIN {
  Win32::SetChildShowWindow(0) if defined &Win32::SetChildShowWindow;
  if($^O =~ /Win32/) {
    require Win32::Process;
    import Win32::Process;
    require Win32::Ole;
    import Win32::Ole;
    use Win32::IProc;
  }
};
my $count;
sub isProcessRunning{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $processObj;
    Win32::Process::Open($processObj,$pid,0) or do { return 0; };
    if (defined $processObj){
      $count++;
      print "wow$count\n";
      return 1;
    } else {
      return 0;
    }
  } else{
    return kill 0 ,$pid;
  }
}

sub isProcessRunning_new{
  my $pid = shift;
  if ($^O =~ /Win32/){
  
    my $objWMI = Win32::OLE->GetObject('winmgmts://./root/cimv2');
    my $procs = $objWMI->InstancesOf('Win32_Process');
    
    foreach my $p (in $procs) {
        if ($p->ProcessID =~ $pid){
          $count++;
          print "wow$count\n";
          return 1;
        }
    }
  print "no object exists\n"; 
  return 0;  
    
  
  
  } else{
    return kill 0 ,$pid;
  }
}


sub isProcessRunning_old{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $command = 'tasklist /FI "PID eq '. $pid. '"';
    my $status = `$command`;
    #If we are not able to run tasklist for any reason we give it benefit of doubt and assume that process is running
    my $exit_value = $? >> 8;
    if ($exit_value != 0) {
      
      return 1;
    }
    return ($status =~ /$pid/) ? 1 : 0;
  } else{
    return kill 0 ,$pid;
   }
}

# Child Process Objects Map
my %CHILD_PROC_OBJ = ();

sub isProcessRunning_try{
  my $pid = shift;
  if ($^O =~ /Win32/){
    my $chld_proc_obj = $CHILD_PROC_OBJ{$pid};
    
    if (defined $chld_proc_obj){
      
      my $exitcode;
      $chld_proc_obj->GetExitCode($exitcode);
      return Win32::Process::STILL_ACTIVE() eq $exitcode ? 1:0;
      
    }else{
	    my $processObj;
	    Win32::Process::Open($processObj,$pid,0) or do { return 0; };
	    if (defined $processObj){
	      $CHILD_PROC_OBJ{$pid} = $processObj;
	      return 1;
	    } else {
	      return 0;
	    }
    }
  } else{
    return kill 0 ,$pid;
  }
}

my $pid = $$;
while(1){ 
  my $result = isProcessRunning_old($pid);
  $count++;
  print "wow$count\n";
  select(undef,undef,undef, 0.5);
}