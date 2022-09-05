<?php
header('Content-Type: application/json');
include "./db.php";


$toKeyWord = $_POST['toKeyWord'];
$keyUniq = $_POST['keyUniq'];
$toKeyUniq = $_POST['toKeyUniq'];


$stmt = $db->prepare("UPDATE keyword SET keyword= ?, keyuniq= ? WHERE keyuniq= ? ");
$stmt->bind_param("sss", $toKeyWord, $toKeyUniq, $keyUniq );
$result = $stmt->execute();

echo json_encode([
'success' => $result
]);
