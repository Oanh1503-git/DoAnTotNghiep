<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

$database = new Database();
$conn = $database->Connect();

$hoadon = new HoaDon($conn);

// Kiểm tra đầu vào
if (!isset($_GET['MaKhachHang']) || !isset($_GET['TrangThai'])) {
    echo json_encode([
        "success" => false,
        "message" => "Thiếu MaKhachHang hoặc TrangThai"
    ]);
    exit;
}

$hoadon->MaKhachHang = $_GET['MaKhachHang'];
$hoadon->TrangThai = (int)$_GET['TrangThai'];

// Gọi hàm lấy mã hóa đơn lớn nhất
$maHoaDon = $hoadon->getMaxMaHoaDonByKhachHangTrangThai();

if ($maHoaDon !== null) {
    echo json_encode([
        "success" => true,
        "ma_hoa_don" => (int)$maHoaDon
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Không tìm thấy hóa đơn phù hợp"
    ]);
}
?>
