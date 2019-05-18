<?php
    // echo $_SESSION['username'];
    // die();
    session_start();
    if(!isset($_SESSION['username']) || $_SESSION['username'] == ""){
        header("Location: index.php");
        die();
    }
    require_once("jwtutils.php");
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>OJAK Pay: Home</title>
    <style>
        body{
            background-image: url("resources/image/logo.jpg");
            background-repeat: no-repeat;
            background-size: 100% 100vh;
        }

        form{
            /* border: 2px inset black; */
            /* width: 80vw; */
            /* padding-top: 20px; */
            /* padding-bottom: 20px; */
            /* background-color: cornflowerblue; */
        }
        h1{
            margin-top: 10vh;
            margin-bottom: 10vh;
        }
        h3{
            margin-top: 0px;
            display: inline;
        }
        button#logoutbutton{
            position: absolute;
            right: 10px;
            top: 10px;
        }
        button#homebutton{
            position: absolute;
            left: 10px;
            top: 10px;
        }
    </style>
</head>
<body>
    <form action="controller.php" method="post">
        <input type="hidden" value= "logout" name="formname">
        <button type="submit" id='logoutbutton'>LOGOUT</button>
    </form>
    <a href="home.php"><button id='homebutton'>HOME</button></a>
    <center>
        <br/><br/><br/><br/><br/><br/><br/><br/>
        <fieldset>
            <?php if($_SESSION['role'] != 'admin') {?>
            <legend><h3>Detil Wallet</h3></legend>
            Nama Pemilik: <?php echo $_SESSION['username']." (".$_SESSION['nohp'].")" ?><br/>
            Saldo: Rp <?php 
                $serviceURL = "/api/v1/wallet/".$_SESSION['nohp'];
                $payload = json_encode(['sub'=> $_SESSION['nohp'], 'name'=>$_SESSION['username'], 'rol'=>'USER', 'atp'=>$_SESSION['role']]);
                $res = json_decode(callAPI($serviceURL, createJWT($payload), "GET"));
                echo $res->{'balance'};
            } else { ?>

            <legend><h3>Daftar Wallet</h3></legend>
            <?php
                $serviceURL = "/api/v1/wallet";
                $payload = json_encode(['sub'=>'ROOT', 'name'=>'ROOT', 'rol'=>'ADMIN', 'atp'=>'']);
                $res = json_decode(callAPI($serviceURL, createJWT($payload), "GET"));
            
                foreach($res as $data){
                    // foreach($datas as $data){
                        echo "Wallet Number: " . $data->{'wallet_number'} . "(" . $data->{'type'} .")<br/>";
                        echo "Balance: " . $data->{'balance'} . "<br/><br/>";
                    // }
                }
                
            }?>
            
            <br/>
        </fieldset>
    </center>
</body>
</html>