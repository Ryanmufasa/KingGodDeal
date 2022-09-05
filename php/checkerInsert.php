
<?php
header('Content-Type: application/json');
include "./db.php";

$insertList = $_POST['insertList'];

// mysqli 이용한 구현
$stmt = "use hotdeal";

$stmt="INSERT INTO checker VALUES $insertList";



# 쿼리 실행
//$result = $stmt->execute();
$result = mysqli_query($db, $stmt);
$enco="";






# mysqli_fetch_array 사용 옵션 : MYSQLI_NUM : 키를 숫자로 사용 $row[0], MYSQLI_ASSOC : 키를 데이터키로 사용 $row["col1"], MYSQLI_BOTH : 둘다 사용


    $enco="INSERT INTO checker VALUES $insertList";
    iconv("ecukr", "UTF-8", $enco); //utf-8로 인코딩
   

      echo "$enco";






mysqli_close($db);



