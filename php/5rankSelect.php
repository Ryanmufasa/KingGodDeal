<?php
header('Content-Type: application/json');
include "./db.php";

$stmt = "use hotdeal";
$stmt = "SELECT * FROM contents WHERE condate > DATE_ADD(NOW(), interval-2 DAY)  ORDER BY consuggest DESC LIMIT 5";


# 쿼리 실행
//$result = $stmt->execute();
$result = mysqli_query($db, $stmt);
$result_array = array();

# 루프 돌며 배열에 오브젝트 하나씩 담기
# mysqli_fetch_array 사용 옵션 : MYSQLI_NUM : 키를 숫자로 사용 $row[0], MYSQLI_ASSOC : 키를 데이터키로 사용 $row["col1"], MYSQLI_BOTH : 둘다 사용

while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
    $result_array[] = $row;
}
# 결과값 json 형식으로 변환
echo json_encode($result_array);

mysqli_close($db);
