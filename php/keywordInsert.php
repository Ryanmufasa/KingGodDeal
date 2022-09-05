<?php
header('Content-Type: application/json');
include "./db.php";

$memId = $_POST['memId'];
$keyWord = $_POST['keyWord'];
$keyUniq = $memId."%_%".$keyWord;


$stmt = $db->prepare("INSERT INTO keyword (memid, keyword, keyuniq) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $memId, $keyWord, $keyUniq );
$result = $stmt->execute();

echo json_encode([
'success' => $result
]);
