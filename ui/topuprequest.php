<?php
    // echo $_SESSION['username'];
    // die();
    session_start();
    if(!isset($_SESSION['username']) || $_SESSION['username'] == "" || $_SESSION['role']!='admin'){
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
        <?php
            if(isset($_SESSION['notification']) && $_SESSION['notification'] != ""){
                echo $_SESSION['notification'];
                $_SESSION['notification'] = "";
                unset($_SESSION['notification']);
            }
            echo "<br/><br/>";
        ?>
        <?php 
            $serviceURL = "/api/v1/transaction/topup";
            $payload = json_encode(['sub'=>'ROOT', 'name'=>'ROOT', 'rol'=>'ADMIN', 'atp'=>'']);
            $res = json_decode(callAPI($serviceURL, createJWT($payload), "GET"));

            foreach($res as $data){
                if($data->status == "pending"){
        ?>
                <form style="border: 2px solid black; margin-bottom: 25px;" action="controller.php" method="post">
                    <h3><?php echo $data->transaction_id ?></h3>
                    <p>Wallet Number: <?php echo $data->wallet_number ?></p>
                    <p>Top up Balance: <?php echo $data->topup_balance ?></p>
                    <input type="hidden" value= "topupconfirmation" name="formname">
                    <input type="hidden" value= "<?php echo $data->transaction_id ?>" name="transactionID">
                    <input type="submit" name="buttonval" value="cancel">
                    <input type="submit" name="buttonval" value="confirm"><br/><br/><br/>
                </form>
        <?php }} ?>
    </center>
</body>
</html>