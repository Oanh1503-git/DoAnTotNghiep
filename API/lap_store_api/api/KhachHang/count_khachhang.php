<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

// Include cấu hình và model
include_once('../../config/database.php');
include_once('../../model/khachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect();

// Khởi tạo Khachhang model
$khachhang = new Khachhang($conn);

// Gọi hàm đếm số lượng
$soLuong = $khachhang->DemKhachHang();

if ($soLuong !== false) {
    echo json_encode([
        'success' => true,
        'so_luong' => $soLuong
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Không thể lấy dữ liệu'
    ]);
}
?>
