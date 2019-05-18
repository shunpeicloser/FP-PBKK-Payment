<?php
    // echo $_SESSION['username'];
    // die();
    session_start();
    if(!isset($_SESSION['username']) || $_SESSION['username'] == ""){
        header("Location: index.php");
        die();
    }
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
            <legend><h3>Masukkan Nominal</h3></legend>
            <form action="controller.php" method="post">
                <input type="number" placeholder="0" name="topupvalue">

                <input type="hidden" value= "topup" name="formname"><br/><br/>
                <button type="submit">Top Up</button>
            </form>
        </fieldset>
    </center>
</body>
</html>