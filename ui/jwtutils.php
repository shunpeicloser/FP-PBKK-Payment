<?php
    function createJWT($payload, $secretkey){
        $header = json_encode(['typ' => 'JWT', 'alg' => 'HS512']);
        $header = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($header));
        $payload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));
        $signature = hash_hmac('sha512', $header . "." . $payload, $secretkey, true);
        $signature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        $jwt = $header . "." . $payload . "." . $signature;
        return $jwt;
    }

    function callAPI($url, $jwt){

    }

?>