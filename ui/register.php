<?php
    // echo $_SESSION['username'];
    // die();
    session_start();
    if(isset($_SESSION['username']) && $_SESSION['username'] != ""){
        header("Location: home.php");
        die();
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>OJAK Pay</title>
    <style>
        body{
            background-image: url("resources/image/logo.jpg");
            background-repeat: no-repeat;
            background-size: 100% 100vh;
        }

        fieldset{
            /* border: 2px inset black; */
            width: 50vw;
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
        }
        button#regbutton{
            position: absolute;
            right: 10px;
            top: 10px;
        }
    </style>
</head>
<body>
    <a href='register.php'><button id='regbutton'>DAFTAR</button></a>
    <center>
    <h1>OJAK Pay</h1><br/>
    <form action="controller.php" method="post">
        <fieldset>
            <input type="text" name="username" placeholder="username" autofocus><br/><br/>
            <input type="password" name="password" placeholder="password"><br/><br/>
            <input type="text" name="nohp" placeholder="No. HP"><br/><br/>
            <input type="radio" name="role" value="customer"> Customer<br/><br/>
            <input type="radio" name="role" value="driver"> Driver<br/><br/>
            <input type="radio" name="role" value="restaurant"> Restaurant<br/><br/>
            <input type="hidden" value= "register" name="formname">
        
            <button type="submit">LOGIN</button>
        </fieldset>
    </form>
    <?php
        if(isset($_SESSION['error']) && $_SESSION['error'] != ""){
            echo $_SESSION['error'];
            $_SESSION['error'] = "";
            unset($_SESSION['error']);
        }
    ?>
    </center>
</body>
</html>