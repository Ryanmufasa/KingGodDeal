<?php
header('Content-Type: application/json');
include "./db.php";

$id = $_POST['memId'];
// mysqli 이용한 구현
$stmt = "use hotdeal";

$timestamp = strtotime("-2 days");
$before2=date("Y/m/d", $timestamp);
//echo "현재로부터 2일 전 :$before2", "</br>";
//$timestamp1=.date("Y-m-d H:i:s", $timestamp).;
$stmt = "SELECT * FROM keyword WHERE memid = '$id' ORDER BY keydate desc";
//$stmt="select * from contents  where condate >'$before2'";

//

# 쿼리 실행
//$result = $stmt->execute();
$result = mysqli_query($db, $stmt);
$enco="";
# 루프 돌며 배열에 오브젝트 하나씩 담기
# mysqli_fetch_array 사용 옵션 : MYSQLI_NUM : 키를 숫자로 사용 $row[0], MYSQLI_ASSOC : 키를 데이터키로 사용 $row["col1"], MYSQLI_BOTH : 둘다 사용
// echo mysqli_num_rows($result) , "</br>"; //결과 몇개인지
while($row = mysqli_fetch_array($result)) {
    //echo $row['keyword'];
    //$result_array[] = $row;
    $enco=$row['keyword'];
    iconv("ecukr", "UTF-8", $enco); //utf-8로 인코딩
    //echo $enco;
   // json_encode("contitle like '%$enco%' || ");
   echo "contitle like '%",$enco,"%' || ";
   // echo utf8_encode("contitle like '%",$enco,"%' || ");
   // echo utf8_decode("contitle like '%",$enco,"%' || ");
    //echo json_encode($row['keyword']);
}

echo "'안녕'";//이게 있어야 에러안남 자리 채우는용
# 결과값 json 형식으로 변환
//echo json_encode($result_array);

mysqli_close($db);
