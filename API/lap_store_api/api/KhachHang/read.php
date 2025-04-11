<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/khachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp Khachhang với kết nối PDO
$khachhang = new Khachhang($conn);

// Lấy tất cả khách hàng
$getAllKhachHang = $khachhang->GetAllKhachHang();

$num = $getAllKhachHang->rowCount();

if($num>0){
    $khachhang_array =[];
    $khachhang_array['khachhang'] =[];

    while($row = $getAllKhachHang->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $khachhang_item = array(
            'MaKhachHang'=> $MaKhachHang,
            'HoTen'=> $HoTen,
            'GioiTinh'=>$GioiTinh,
            'NgaySinh'=>$NgaySinh,
            'Email'=> $Email,
            'SoDienThoai'=> $SoDienThoai,
        );
        array_push($khachhang_array['khachhang'],$khachhang_item);
    }
    echo json_encode($khachhang_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>