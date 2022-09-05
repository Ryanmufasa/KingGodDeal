<?php
  header('Content-Type: application/json');
  include "./db.php";
  $sql = "SELECT * FROM contents WHERE condate > DATE_ADD(NOW(), interval-2 DAY)  ORDER BY consuggest DESC LIMIT 20";

  $res = mysqli_query($db, $sql);
  $res_arr = array();

  while($row = mysqli_fetch_array($res, MYSQLI_ASSOC)){
    $res_arr[] = $row;
  }

  echo json_encode($res_arr);

  mysqli_close($db);
?>