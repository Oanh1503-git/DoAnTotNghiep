<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/mausac.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp Khachhang với kết nối PDO
$mausac = new mausac($conn);

// Lấy tất cả khách hàng
$getAllMauSac = $mausac->GetAllMauSac();

$num = $getAllMauSac->rowCount();

if($num>0){
    $mausac_array =[];
    $mausac_array['mausac'] =[];

    while($row = $getAllMauSac->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $mausac_item = array(
            'MaTMaMauSacinh'=> $MaMauSac,
            'TenMauSac'=> $TenMauSac
        );
        array_push($mausac_array['mausac'],$mausac_item);
    }
    echo json_encode($mausac_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>