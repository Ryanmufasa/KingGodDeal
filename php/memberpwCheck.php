<?php
header('Content-Type: application/json');
include "./db.php";

$pw = $_POST['memPw'];
$id = $_POST['memId'];

$stmt = "SELECT * FROM member WHERE memid='$id' and mempw = '$pw'";


$result = mysqli_query($db, $stmt);
$row = mysqli_fetch_array($result);
echo json_encode([
'success' => $row
]);
