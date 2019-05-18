<?php
    $username="user2";
    $nohp= "02380235";
    $role = "customer";
    
    $file = fopen("user.file","a");
    echo fprintf($file,"%s %s %s\n",$username,$nohp,$role);
?>