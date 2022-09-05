<?php

$db_name = "hotdeal";
$db_server = "shared00.iptime.org:3307";
$db_user = "hotdeal";
$db_pass = "Gktelf12!@";

// PDO 이용한 구현
// $db = new PDO("mysql:host={$db_server};dbname={$db_name};charset=utf8", $db_user, $db_pass);
// $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
// $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

// mysqli 이용한 구현
$db = mysqli_connect($db_server, $db_user, $db_pass, $db_name);

if (!$db) {
	die("Connection failed: " . mysqli_connect_error());
}else{
    //echo "접속 성공";
}
?>