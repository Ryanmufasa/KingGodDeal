<?php
  header('Content-Type: application/json');
  include "./db.php";
  $searchIdx = $_POST['searchIdx'];
  $sql = "SELECT * FROM contents WHERE REPLACE(contitle, ' ', '') LIKE '%$searchIdx%' ORDER BY condate DESC";

  $res = mysqli_query($db, $sql);
  $res_arr = array();

  while($row = mysqli_fetch_array($res, MYSQLI_ASSOC)){
    $res_arr[] = $row;
  }

  echo json_encode($res_arr);

  mysqli_close($db);
?>