<?php
header('Content-Type: application/json');
include "./db.php";

$memId = $_POST['memId'];
$memPw = $_POST['memPw'];


$stmt = $db->prepare("INSERT INTO member (memid, mempw, memtype) VALUES (?, ?, 'nomal')");
// $result = $stmt->execute([$name, $age, $createdAt]);
$stmt->bind_param("ss", $memId, $memPw);
$result = $stmt->execute();

echo json_encode([
'success' => $result
]);
