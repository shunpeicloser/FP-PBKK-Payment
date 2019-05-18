<?php
    require_once("jwtutils.php");
    
    $serviceURL = "/api/v1/wallet/6288804862379";
    $payload = json_encode(['sub'=>'6288804862379', 'name'=>'Alcredo', 'rol'=>'USER', 'atp'=>'customer']);
    
    $jwt = createJWT($payload, $secretkey);
    echo $jwt;
    $test = callAPI($serviceURL, $jwt, "GET");
    echo $test;
    
    // $username="user2";
    // $nohp= "02380235";
    // $role = "customer";
    
    // $file = fopen("user.file","a");
    // echo fprintf($file,"%s %s %s\n",$username,$nohp,$role);
?>