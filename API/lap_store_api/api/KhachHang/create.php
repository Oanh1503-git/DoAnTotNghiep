<?php
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/khachhang.php');

$database = new Database();
$conn = $database->Connect();
$khachhang = new Khachhang($conn);

// Lấy dữ liệu từ client
$data = json_decode(file_get_contents("php://input"));

if (!$data) {
    echo json_encode(['success' => false, 'message' => 'Dữ liệu không hợp lệ']);
    exit;
}

$khachhang->MaKhachHang = $data->MaKhachHang ?? null;
$khachhang->HoTen = $data->HoTen ?? '';
$khachhang->GioiTinh = $data->GioiTinh ?? '';
$khachhang->NgaySinh = $data->NgaySinh ?? '';
$khachhang->Email = $data->Email ?? '';
$khachhang->SoDienThoai = $data->SoDienThoai ?? '';

if ($khachhang->AddKhachHang()) {
    echo json_encode(['success' => true, 'message' => 'Khách hàng được tạo thành công']);
} else {
    echo json_encode(['success' => false, 'message' => 'Tạo khách hàng thất bại']);
}
