<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/giohang.php');
include_once('../../model/sanpham.php'); 

$database = new database();
$conn = $database->Connect();

$giohang = new giohang($conn);

$input = file_get_contents("php://input");
$data = json_decode($input);

if (is_null($data)) {
    echo json_encode(array(
        'success' => false,
        'message' => 'Dữ liệu JSON không hợp lệ hoặc rỗng',
        'raw_input' => $input
    ));
    return;
}

$giohang->MaKhachHang = $data->MaKhachHang ?? null;
$giohang->MaSanPham = $data->MaSanPham ?? null;
$giohang->SoLuong = $data->SoLuong ?? 0;
$giohang->TrangThai = $data->TrangThai ?? 0;

if (empty($giohang->MaKhachHang) || empty($giohang->MaSanPham)) {
    echo json_encode(array(
        'success' => false,
        'message' => 'Thiếu MaKhachHang hoặc MaSanPham'
    ));
    return;
}

if ($giohang->AddGioHang()) {
    echo json_encode(array(
        'success' => true,
        'message' => 'Thêm vào giỏ hàng thành công'
    ));
} else {
    echo json_encode(array(
        'success' => false,
        'message' => 'Không thể thêm vào giỏ hàng'
    ));
}
?>
