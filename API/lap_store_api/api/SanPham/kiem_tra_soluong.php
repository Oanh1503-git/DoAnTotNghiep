<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Kết nối database
$database = new Database();
$conn = $database->Connect();
$sanPham = new SanPham($conn);

// Kiểm tra đầu vào
if (!isset($_GET['MaKhachHang']) || !isset($_GET['MaSanPham'])) {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'Thiếu MaKhachHang hoặc MaSanPham'
    ]);
    exit;
}

$sanPham->MaKhachHang = $_GET['MaKhachHang'];
$sanPham->MaSanPham = $_GET['MaSanPham'];

// Gọi hàm xử lý
$result = $sanPham->KiemTraSoLuong();
echo json_encode($result, JSON_UNESCAPED_UNICODE);
?>
