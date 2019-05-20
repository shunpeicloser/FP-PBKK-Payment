<?php
    require_once("jwtutils.php");
    $serviceURL = "/api/v1/wallet";
    $payload = json_encode(['sub'=>'ROOT', 'name'=>'ROOT', 'rol'=>'ADMIN', 'atp'=>'']);
    $res = json_decode(callAPI($serviceURL, createJWT($payload), "GET"));
    foreach($res as $datas){
        foreach($datas as $data)
            echo $data . "<br/>";
    }
    
    // $username="user2";
    // $nohp= "02380235";
    // $role = "customer";
    
    // $file = fopen("user.file","a");
    // echo fprintf($file,"%s %s %s\n",$username,$nohp,$role);
?>