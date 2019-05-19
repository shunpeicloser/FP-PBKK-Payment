<?php
    session_start();
    require_once("jwtutils.php");
    $formname = $_POST["formname"];

    if($formname == "login"){
        $handle= fopen("user.file", "r");
        $fusername;
        $fnohp;
        $frole;
        for($i=0;($line = fgets($handle));$i++){
            list($fusername[$i],$fnohp[$i],$frole[$i])=preg_split('/ /',$line);
            $frole[$i] = substr($frole[$i],0,-1);
        }
        $username = $_POST["username"];
        $password = $_POST["password"];
        $status = "accepted"; // user auth here
        if($status == "accepted"){
            $get=0;
            for($i=0;$i<sizeof($fusername);$i++){
                if(strcmp($fusername[$i],$username)==0){
                    $get=1;
                    $_SESSION['username']=$fusername[$i];
                    $_SESSION['nohp']=$fnohp[$i];
                    $_SESSION['role']=$frole[$i];
                    break;
                }
            }
            if($get==1) header("Location: home.php");
            else{
                $_SESSION['error'] = "<br/><div id='errormsg' style='border: 2px solid black; width: 20vw; background-color: red;'>Username/password tidak valid</div>";
                header("Location: index.php");
            }
        }
        else{
            $_SESSION['error'] = "<br/><div id='errormsg' style='border: 2px solid black; width: 20vw; background-color: red;'>Username/password tidak valid</div>";
            header("Location: index.php");
        }
        die();
    }
    else if($formname == "register"){
        $new_username = $_POST['username'];
        $new_password = $_POST['password'];
        $new_nohp = $_POST['nohp'];
        $new_role = $_POST['role'];
        $_SESSION['username']=$new_username;
        $_SESSION['nohp']=$new_nohp;
        $_SESSION['role']=$new_role;
        $file = fopen("user.file","a");
        fprintf($file,"%s %s %s\n",$new_username,$new_nohp,$new_role);
        header("Location: home.php");
        die();
    }

    else if($formname == "logout"){
        session_unset();
        session_destroy();
        header("Location: index.php");
        die();
    }
    else if($formname == "topup"){
        $body = json_encode(['topup_balance'=>(int)$_POST['topupvalue']]);
        $serviceURL = "/api/v1/transaction/topup";
        $payload = json_encode(['sub'=>$_SESSION['nohp'], 'name'=>$_SESSION['username'], 'rol'=>'USER', 'atp'=>$_SESSION['role']]);
        $res = json_decode(callAPI($serviceURL, createJWT($payload), "POST", $body));
        $_SESSION['notification'] = "<br/><div style='border: 2px solid black; background-color: blue;'>Top Up Balance: $res->{'balance'}<br/>Top Up ID: $res->{'transaction_id'}</div>";
        die();
    }
?>