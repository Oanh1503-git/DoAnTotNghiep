<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Kết nối và load model
include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// ✅ Tạo kết nối CSDL
$database = new Database();
$db = $database->Connect();

// ✅ Tạo đối tượng SanPham
$sanPham = new SanPham($db);

// ✅ Kiểm tra class và method (tùy chọn kiểm tra lỗi)
if (!class_exists('SanPham')) {
    die("❌ Không tìm thấy class SanPham trong model.");
}
if (!method_exists($sanPham, 'getSoLuongKho')) {
    die("❌ Không tìm thấy hàm getSoLuongKho trong class SanPham.");
}

// ✅ Nhận tham số
$MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : die(json_encode(["message" => "Thiếu MaSanPham"]));

// ✅ Gọi hàm lấy số lượng kho
$soLuong = $sanPham->getSoLuongKho($MaSanPham);

if ($soLuong !== null) {
    echo json_encode(["SoLuong" => (int)$soLuong]);
} else {
    echo json_encode(["message" => "Không tìm thấy sản phẩm"]);
}
?>
