<?php
// SanPhamYeuThich/create.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once('../../config/database.php');
include_once('../../model/SanPhamYeuThich.php');

$database = new Database();
$db = $database->Connect();

$yeuthich = new SanPhamYeuThich($db);

$data = json_decode(file_get_contents("php://input"));

if (!empty($data->MaSanPham) && !empty($data->MaKhachHang)) {
    $yeuthich->MaSanPham = $data->MaSanPham;
    $yeuthich->MaKhachHang = $data->MaKhachHang;

    if ($yeuthich->create()) {
        http_response_code(201);
        echo json_encode(array("message" => "Favorite added successfully."));
    } else {
        http_response_code(503);
        echo json_encode(array("message" => "Unable to add favorite."));
    }
} else {
    http_response_code(400);
    echo json_encode(array("message" => "Incomplete data."));
}
?>