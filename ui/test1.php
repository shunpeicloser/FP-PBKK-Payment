<?php
    require_once("jwtutils.php");
    $serviceURL = "/api/v1/wallet";
    $payload = json_encode(['sub'=>'ROOT', 'name'=>'ROOT', 'rol'=>'ADMIN', 'atp'=>'']);
    $body = json_encode(["wallet_number"=> "6288804862872","type"=> "customer"]);
    echo createJWT($payload)."<br/>";
    $res = json_decode(callAPI($serviceURL, createJWT($payload), "POST",$body));
    echo $res."<br/>";
    
?>