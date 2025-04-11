<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');
header('Access-Control-Allow-Credentials: true');

include_once('../../config/database.php');
include_once('../../model/khachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect();

// Khởi tạo lớp Khachhang với kết nối PDO
$khachhang = new Khachhang($conn);

$data = json_decode(file_get_contents("php://input"));

if (!$data) {
    echo json_encode(array('message' => 'Invalid input data'));
    exit;
}

$khachhang->MaKhachHang = $data->MaKhachHang;
$khachhang->HoTen = $data->HoTen;
$khachhang->GioiTinh = $data->GioiTinh;
$khachhang->NgaySinh = $data->NgaySinh;
$khachhang->SoDienThoai = $data->SoDienThoai;
$khachhang->Email = $data->Email;

if ($khachhang->UpdateKhachHang()) {
    echo json_encode(array('message' => 'Khach Hang Updated'));
} else {
    echo json_encode(array('message' => 'Khach Hang Not Updated'));
}
?>