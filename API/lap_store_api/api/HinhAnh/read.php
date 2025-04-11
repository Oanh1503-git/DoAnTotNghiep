<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hinhanh.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp Khachhang với kết nối PDO
$hinhanh = new HinhAnh($conn);

// Lấy tất cả khách hàng
$getAllHinhAnh = $hinhanh->GetAllHinhAnh();

$num = $getAllHinhAnh->rowCount();

if($num>0){
    $hinhanhh_array =[];
    $hinhanhh_array['hinhanh'] =[];

    while($row = $getAllHinhAnh->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $hinhanh_item = array(
            'MaHinhAnh'=> $MaHinhAnh,
            'DuongDan'=> $DuongDan,
            'MaSanPham'=> $MaSanPham
        );
        array_push($hinhanhh_array['hinhanh'],$hinhanh_item);
    }
    echo json_encode($hinhanhh_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>