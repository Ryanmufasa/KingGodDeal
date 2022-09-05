<?php
header('Content-Type: application/json');
include "./db.php";

$id = $_POST['memId'];

$stmt = "SELECT * FROM member WHERE memid = '$id'";


$result = mysqli_query($db, $stmt);
$row = mysqli_fetch_array($result);

echo json_encode([
'success' => $row
]);
