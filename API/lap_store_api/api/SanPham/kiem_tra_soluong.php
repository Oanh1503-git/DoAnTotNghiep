<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

$database = new Database();
$conn = $database->Connect();
$sanPham = new SanPham($conn);

// Kiểm tra tham số
if (!isset($_GET['MaKhachHang']) || !isset($_GET['MaSanPham'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Thiếu MaKhachHang hoặc MaSanPham'
    ]);
    exit;
}

$sanPham->MaKhachHang = $_GET['MaKhachHang'];
$sanPham->MaSanPham = $_GET['MaSanPham'];

// Gọi hàm kiểm tra
$result = $sanPham->KiemTraSoLuong();
echo json_encode($result, JSON_UNESCAPED_UNICODE);
