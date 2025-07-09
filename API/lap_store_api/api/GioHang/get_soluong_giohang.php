<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/giohang.php');

$database = new Database();
$db = $database->Connect();

$gioHang = new GioHang($db);

$MaKhachHang = $_GET['MaKhachHang'] ?? null;
$MaSanPham = $_GET['MaSanPham'] ?? null;

if ($MaKhachHang && $MaSanPham) {
    $soLuong = $gioHang->getSoLuongTrongGioHang($MaKhachHang, $MaSanPham);
    echo json_encode(["SoLuong" => $soLuong]);
} else {
    echo json_encode(["message" => "Thiếu tham số MaKhachHang hoặc MaSanPham"]);
}
?>
