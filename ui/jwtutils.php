<?php
    include "curl.h";
    function createJWT($payload, $secretkey){
        $header = json_encode(['alg' => 'HS512', 'typ' => 'JWT']);
        $header = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($header));
        $payload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));
        $signature = hash_hmac('sha512', $header . "." . $payload, $secretkey, true);
        $signature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        $jwt = $header . "." . $payload . "." . $signature;
        return $jwt;
    }

    function callAPI($baseurl, $serviceurl, $jwt, $method){
        $opt = array(
            'http' => array(
                'method' => $method,
                'header' => "Authorization: Bearer ".$jwt
            )
        );

        $context = stream_context_create($opt);
        
        $returned_data = file_get_contents($baseurl.$serviceurl, false, $context);

        return $returned_data;
    }

?>