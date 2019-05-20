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
        }
        $context = stream_context_create($opt);
        $returned_data = file_get_contents($baseurl.$serviceurl, false, $context);
        // var_dump($returned_data);
        return $returned_data;
    }
    // statis pake x-www-form-urlencoded
    function callCustomerAPI($basicval, $username, $password){
        $usertype = ["user", "driver", "restaurant"];
        $opt;
        $url = 'https://rendoru.com/kuliah/pbkk/oauth/token';

        foreach($usertype as $prefix){
            $body = http_build_query(array(
                'grant_type' => 'password',
                'username' => $prefix . "_" . $username,
                'password' => $password
            ));
            // echo $body; die();
            $opt = array(
                'http' => array(
                    'method' => "POST",
                    'header' => "Authorization: Basic $basicval\r\nContent-Type: application/x-www-form-urlencoded\r\n",
                    'content' => $body
                )
            );
            
            $context = stream_context_create($opt);
            $returned_data = file_get_contents($url, false, $context);
            if($returned_data != false){
                break;
            }
        }
        if($returned_data == false){
            return 0;
        }
        $jwt = json_decode($returned_data);
        $jwt = $jwt->access_token;
        $url = 'https://rendoru.com/kuliah/pbkk/oauth/check_token';

        $body = http_build_query(array(
            'token' => $jwt
        ));

        $opt = array(
            'http' => array(
                'method' => "POST",
                'header' => "Authorization: Bearer $jwt\r\nContent-Type: application/x-www-form-urlencoded\r\n",
                'content' => $body
            )
        );

        $context = stream_context_create($opt);
        $returned_data = file_get_contents($url, false, $context);
        $returned_data = json_decode($returned_data, true);
        // $tmp = $returned_data['sub'];
        // var_dump(implode(",", $returned_data)); die();
        $tmp = "$returned_data[sub]";
        // $tmp = (string)$tmp;
        $url = "https://rendoru.com/kuliah/pbkk/users/".$tmp;
        // echo $url; die();
        $opt = array(
            'http' => array(
                'method' => "GET",
                'header' => "Authorization: Bearer $jwt\r\n"
            )
        );
        $context = stream_context_create($opt);
        $returned_data = file_get_contents($url, false, $context);
        $returned_data = json_decode($returned_data, true);
        // var_dump($returned_data); die();
        $ret1 = "$returned_data[no_handphone]";
        $ret2 = "$returned_data[identifier]";
        // var_dump($ret); die();
        return [$ret1, $ret2];
    }

?>