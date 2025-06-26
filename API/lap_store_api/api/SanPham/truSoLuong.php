<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Kết nối database
$database = new Database();
$db = $database->Connect();

// Khởi tạo đối tượng SanPham
$sanpham = new SanPham($db);

// Nhận dữ liệu từ body
$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->MaSanPham) &&
    isset($data->SoLuongCanTru)
) {
    $sanpham->MaSanPham = $data->MaSanPham;
    $soLuongCanTru = (int)$data->SoLuongCanTru;

    $result = $sanpham->truSoLuongTrongKho($soLuongCanTru);
    echo json_encode($result);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Thiếu thông tin MaSanPham hoặc SoLuongCanTru'
    ]);
}
?>
