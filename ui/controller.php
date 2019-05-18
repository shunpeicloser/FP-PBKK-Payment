<?php
    session_start();
    require_once("jwtutils.php");
    $formname = $_POST["formname"];

    if($formname == "login"){
        
        $username = $_POST["username"];
        $password = $_POST["password"];
        $status = "accepted"; // user auth here
        if($status == "accepted"){
            $_SESSION['username'] = $username;
            if($username == "alcredo"){
                $_SESSION['nohp'] = "6288804862379";
            }
            header("Location: home.php");
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
        // add new user here
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
        $isdone = "sukses";
        if($isdone == "sukses"){
            header("Location: walletdetail.php");
        }
        else{
            $_SESSION['error'] = "error apa tulis sini";
            header("Location: topup.php");
        }
        die();
    }
?>