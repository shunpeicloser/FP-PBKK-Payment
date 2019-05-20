<?php
    $secretkey = base64_decode('EmYTCuhxT3$FpXUVXDj*f0e4LbcpDAk^apes1eAuUtUrC3V%1XPSGD^2KPj*u^L&');
    $baseurl = "https://pyradian.me:9443";
    $header = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode(json_encode(['alg' => 'HS512', 'typ' => 'JWT'])));

    function createJWT($payload){
        global $header, $secretkey;
        $payload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));
        $signature = hash_hmac('sha512', $header . "." . $payload, $secretkey, true);
        $signature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        $jwt = $header . "." . $payload . "." . $signature;
        return $jwt;
    }

    function callAPI($serviceurl, $jwt, $method, $body=NULL){
        global $baseurl;
        $opt;
        if($body == NULL){
            $opt = array(
                'http' => array(
                    'method' => $method,
                    'header' => "Authorization: Bearer ".$jwt
                )
            );
        } else {
            $opt = array(
                'http' => array(
                    'method' => $method,
                    'header' => "Content-Type: application/json\r\nAuthorization: Bearer ".$jwt,
                    'content' => $body
                )
            );
            // $request = new HttpRequest();
            // $request->setUrl($baseurl.$serviceurl);
            // $request->setMethod($method);

            // $request->setHeaders(array(
            //     'Authorization' => "Bearer $jwt",
            //     'Content-Type' => 'application/json'
            // ));

            // $request->setBody($body);

            // try {
            //     $response = $request->send();

            //     echo $response->getBody();
            //     die();
            // } catch (HttpException $ex) {
            //     echo $ex;
            // }
            // return $response->getBody();
        }
        $context = stream_context_create($opt);
        $returned_data = file_get_contents($baseurl.$serviceurl, false, $context);
        // var_dump($returned_data);
        return $returned_data;
    }

?>