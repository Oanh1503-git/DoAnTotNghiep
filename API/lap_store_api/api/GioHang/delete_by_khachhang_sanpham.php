<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: DELETE');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Kết nối database
$database = new Database();
$conn = $database->Connect();

// Tạo model
$giohang = new GioHang($conn);

// Nhận dữ liệu từ body JSON
$data = json_decode(file_get_contents("php://input"));

if (isset($data->MaKhachHang) && isset($data->MaSanPham)) {
    $giohang->MaKhachHang = $data->MaKhachHang;
    $giohang->MaSanPham = $data->MaSanPham;

    if ($giohang->deleteByKhachHangAndSanPham()) {
        echo json_encode(["success" => true, "message" => "Xóa sản phẩm khỏi giỏ hàng thành công"]);
    } else {
        echo json_encode(["success" => false, "message" => "Không thể xóa sản phẩm khỏi giỏ hàng"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Thiếu MaKhachHang hoặc MaSanPham"]);
}
