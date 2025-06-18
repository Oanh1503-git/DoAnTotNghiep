<?php
// SanPhamYeuThich/check.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/SanPhamYeuThich.php');

$database = new Database();
$db = $database->Connect();

$yeuthich = new SanPhamYeuThich($db);

$MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : die(json_encode(array("message" => "Missing MaSanPham parameter.")));
$MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(array("message" => "Missing MaKhachHang parameter.")));

$isFavorite = $yeuthich->check($MaSanPham, $MaKhachHang);

http_response_code(200);
echo json_encode(array("isFavorite" => $isFavorite));
?>