<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');
header('Access-Control-Allow-Credentials: true');

include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect();

// Khởi tạo lớp Giohang với kết nối PDO
$giohang = new giohang($conn);

// Đọc dữ liệu JSON từ input
$data = json_decode(file_get_contents("php://input"));

if (!$data || !is_array($data->giohangList)) {
    echo json_encode(array('message' => 'Invalid input data'));
    exit;
}

// Cập nhật từng giỏ hàng trong danh sách
foreach ($data->giohangList as $giohangData) {
    $giohang->MaGioHang = $giohangData->MaGioHang;
    $giohang->MaKhachHang = $giohangData->MaKhachHang;
    $giohang->MaSanPham = $giohangData->MaSanPham;
    $giohang->SoLuong = $giohangData->SoLuong;
    $giohang->TrangThai = $giohangData->TrangThai;

    if (!$giohang->UpdateGioHang()) {
        echo json_encode(array('message' => 'Failed to update Gio Hang with MaGioHang: ' . $giohangData->MaGioHang));
        exit;
    }
}

echo json_encode(array('message' => 'All Gio Hang Updated Successfully'));
?>
