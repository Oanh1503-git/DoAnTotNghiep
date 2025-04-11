<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp Khachhang với kết nối PDO
$giohang = new giohang($conn);

// Lấy tất cả khách hàng
$getAllGioHang = $giohang->GetAllGioHang();

$num = $getAllGioHang->rowCount();

if($num>0){
    $giohang_array =[];
    $giohang_array['giohang'] =[];

    while($row = $getAllGioHang->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $giohang_item = array(
            'MaGioHang'=> $MaGioHang,
            'MaKhachHang'=> $MaKhachHang,
            'MaSanPham'=> $MaSanPham,
            'SoLuong'=> $SoLuong,
            'TrangThai'=> $TrangThai
        );
        array_push($giohang_array['giohang'],$giohang_item);
    }
    echo json_encode($giohang_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>