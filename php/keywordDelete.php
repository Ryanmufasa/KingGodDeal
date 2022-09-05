<?php
header('Content-Type: application/json');
include "./db.php";

$keyUniq = $_POST['keyUniq'];


$stmt = $db->prepare("DELETE FROM keyword WHERE keyuniq = ?");
$stmt->bind_param("s", $keyUniq);
$result = $stmt->execute();
// $result = $stmt->execute([$id]);

echo json_encode([
'success' => $result
]);

?>