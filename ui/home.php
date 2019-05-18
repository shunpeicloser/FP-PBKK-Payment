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
            display: inline;
            margin-top: 0px;
        }
        button#logoutbutton{
            position: absolute;
            right: 10px;
            top: 10px;
        }
        span.featurebutton{
            position: relative;
            font-size: 40px;
        }
        span button{
            width: 40vw;
            height: 15vw;
        }
        span form{
            display: inline;
        }
    </style>
</head>
<body>
    <form action="controller.php" method="post">
        <input type="hidden" value= "logout" name="formname">
        <button type="submit" id='logoutbutton'>LOGOUT</button>
    </form>
    <center>
        <br/><br/><br/><br/><br/><br/><br/><br/>
        <fieldset>
            <legend><h3>Fitur</h3></legend>
            <?php if($_SESSION['role'] != 'admin'){ ?>
            <span class="featurebutton">
                <a href='walletdetail.php'><button>Cek Wallet</button></a>
            </span>
            <?php } ?>

            <?php if($_SESSION['role'] == 'customer'){ ?>
            <span class="featurebutton">
                <a href='topup.php'><button>Top Up</button></a>
            </span>
            <?php } ?>

            <?php if($_SESSION['role'] == 'admin'){ ?>
            <span class="featurebutton">
                <a href='walletdetail.php'><button>Daftar Wallet</button></a>
            </span>
            <span class="featurebutton">
                <a href='topuprequest.php'><button>Top Up Request</button></a>
            </span>
            <?php } ?>
        </fieldset>
    </center>
</body>
</html>