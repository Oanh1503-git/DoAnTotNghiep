<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/diachi.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp diachi với kết nối PDO
$diachi = new DiaChi($conn);

// Lấy tất cả diachi
$getAllDiaChi = $diachi->GetAllDiaChi();

$num = $getAllDiaChi->rowCount();

if($num>0){
    $diachi_array =[];
    $diachi_array['diachi'] =[];

    while($row = $getAllDiaChi->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $diachi_item = array(
            'MaDiaChi'=> $MaDiaChi,
            'ThongTinDiaChi'=> $ThongTinDiaChi,
            'MaKhachHang'=> $MaKhachHang,
            'TenNguoiNhan'=> $TenNguoiNhan,
            'SoDienThoai'=> $SoDienThoai,
            'MacDinh'=> $MacDinh,
        );
        array_push($diachi_array['diachi'],$diachi_item);
    }
    echo json_encode($diachi_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>