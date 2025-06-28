<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/diachi.php');

$database = new Database();
$conn = $database->Connect();
$diachi = new DiaChi($conn);

$data = json_decode(file_get_contents("php://input"));

if (!empty($data->MaKhachHang) && !empty($data->MaDiaChi)) {
    $diachi->MaKhachHang = $data->MaKhachHang;
    $diachi->MaDiaChi = $data->MaDiaChi;

    if ($diachi->SetDiaChiMacDinh()) {
        echo json_encode(["message" => "Cập nhật địa chỉ mặc định thành công"]);
    } else {
        echo json_encode(["message" => "Cập nhật thất bại"]);
    }
} else {
    echo json_encode(["message" => "Thiếu dữ liệu MaKhachHang hoặc MaDiaChi"]);
}
?>
