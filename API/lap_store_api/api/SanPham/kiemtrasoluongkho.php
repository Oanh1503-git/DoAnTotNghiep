<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

$database = new Database();
$db = $database->Connect();

$sanPham = new SanPham($db);

if (!class_exists('SanPham')) {
    die("❌ Không tìm thấy class SanPham trong model.");
}
if (!method_exists($sanPham, 'getSoLuongKho')) {
    die("❌ Không tìm thấy hàm getSoLuongKho trong class SanPham.");
}

$MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : die(json_encode([
    "success" => false,
    "message" => "Thiếu MaSanPham"
]));

$soLuong = $sanPham->getSoLuongKho($MaSanPham);

if ($soLuong !== null) {
    echo json_encode([
        "success" => true,
        "SoLuongTonKho" => (int)$soLuong
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Không tìm thấy sản phẩm"
    ]);
}
?>
