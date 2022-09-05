<?php
header('Content-Type: application/json');
include "./db.php";
$id = $_POST['memId'];
$keyword = $_POST['keyword'];

// mysqli 이용한 구현
$stmt = "use hotdeal";

$timestamp = strtotime("-2 days");
$before2=date("Y/m/d", $timestamp);//현재로부터 2일전
$timestamp2 = strtotime("Now");
$now=date("Y/m/d", $timestamp2);//현재
//$stmt="SELECT * FROM (SELECT * FROM contents  WHERE condate >'$before2' )A  WHERE A.conid  NOT IN (SELECT conid  FROM checker  WHERE memid='$id')";
$stmt="SELECT * FROM (SELECT * FROM contents  WHERE condate >='$before2' AND $keyword)A  WHERE A.conid  NOT IN (SELECT conid  FROM checker  WHERE memid='$id')";



# 쿼리 실행
//$result = $stmt->execute();
$result = mysqli_query($db, $stmt);
$enco="";

$rowCount1=mysqli_num_rows($result);

$rowCount=(int)$rowCount1; //행 개수
if($rowCount<1){
	
	echo "no";
	}

$countgo=1;
# 루프 돌며 배열에 오브젝트 하나씩 담기
# mysqli_fetch_array 사용 옵션 : MYSQLI_NUM : 키를 숫자로 사용 $row[0], MYSQLI_ASSOC : 키를 데이터키로 사용 $row["col1"], MYSQLI_BOTH : 둘다 사용

while($row = mysqli_fetch_array($result)) {
    $enco=$row['conid'];
    iconv("ecukr", "UTF-8", $enco); //utf-8로 인코딩
     if ($countgo == $rowCount){

      echo "('$id',$enco,'$now','$id%_%$enco')";

   } else {

      echo "('$id',$enco,'$now','$id%_%$enco'),";
   }
	$countgo+=1;

}


mysqli_close($db);
