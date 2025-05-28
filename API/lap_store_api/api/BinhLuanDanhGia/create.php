<?php
// BinhLuan/create.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once('../../config/database.php');
include_once('../../model/BinhLuanDanhGia.php');

$database = new Database();
$db = $database->Connect();

$binhluan = new BinhLuanDanhGia($db);

$data = json_decode(file_get_contents("php://input"));

if (
    !empty($data->MaKhachHang) &&
    !empty($data->MaSanPham) &&
    !empty($data->MaHoaDonBan) &&
    !empty($data->SoSao) &&
    !empty($data->NoiDung)
) {
    $binhluan->MaKhachHang = $data->MaKhachHang;
    $binhluan->MaSanPham = $data->MaSanPham;
    $binhluan->MaHoaDonBan = $data->MaHoaDonBan;
    $binhluan->SoSao = $data->SoSao;
    $binhluan->NoiDung = $data->NoiDung;
    $binhluan->NgayDanhGia = date('Y-m-d H:i:s');
    $binhluan->TrangThai = isset($data->TrangThai) ? $data->TrangThai : 1;

    if ($binhluan->create()) {
        http_response_code(201);
        echo json_encode(array("message" => "Comment created successfully."));
    } else {
        http_response_code(503);
        echo json_encode(array("message" => "Unable to create comment."));
    }
} else {
    http_response_code(400);
    echo json_encode(array("message" => "Incomplete data."));
}
?>