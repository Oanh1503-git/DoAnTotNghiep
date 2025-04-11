<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');
header('Access-Control-Allow-Credentials: true');

include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect();

// Khởi tạo lớp Khachhang với kết nối PDO
$giohang = new giohang($conn);

$data = json_decode(file_get_contents("php://input"));

if (!$data) {
    echo json_encode(array('message' => 'Invalid input data'));
    exit;
}

$giohang->MaGioHang = $data->MaGioHang;
$giohang->MaKhachHang = $data->MaKhachHang;
$giohang->MaSanPham = $data->MaSanPham;
$giohang->SoLuong = $data->SoLuong;
$giohang->TrangThai = $data->TrangThai;

if ($giohang->UpdateGioHang()) {
    echo json_encode(array('message' => 'Gio Hang Updated'));
} else {
    echo json_encode(array('message' => 'Gio Hang Not Updated'));
}
?>
