<?php
// Headers cho phép truy cập từ bên ngoài (CORS)
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

// Import kết nối DB và model
include_once('../../config/database.php');
include_once('../../model/khachhang.php');

// Khởi tạo kết nối đến database
$database = new Database();
$db = $database->Connect();

// Khởi tạo đối tượng Khachhang
$khachhang = new Khachhang($db);

// Gọi hàm generate mã khách hàng
$maKhachHang = $khachhang->generateCustomerCode();

// Trả về kết quả JSON
echo json_encode([
    "success" => true,
    "ma_khach_hang" => $maKhachHang
]);
