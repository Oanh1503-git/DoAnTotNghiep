<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Kết nối database
$database = new Database();
$conn = $database->Connect();

// Tạo model
$giohang = new GioHang($conn);

// Nhận dữ liệu từ body JSON
$input = file_get_contents("php://input");
error_log("Received input: " . $input);

$data = json_decode($input);

if (isset($data->MaKhachHang) && isset($data->MaSanPham)) {
    $giohang->MaKhachHang = $data->MaKhachHang;
    $giohang->MaSanPham = $data->MaSanPham;

    error_log("Attempting to delete: MaKhachHang = {$giohang->MaKhachHang}, MaSanPham = {$giohang->MaSanPham}");

    // Kiểm tra xem sản phẩm có tồn tại trong giỏ hàng không
    if (!$giohang->checkSanPhamExistsInGioHang()) {
        error_log("Product not found in cart");
        echo json_encode(["success" => false, "message" => "Sản phẩm không tồn tại trong giỏ hàng"]);
        return;
    }

    if ($giohang->deleteByKhachHangAndSanPham()) {
        error_log("Delete successful");
        echo json_encode(["success" => true, "message" => "Xóa sản phẩm khỏi giỏ hàng thành công"]);
    } else {
        error_log("Delete failed");
        echo json_encode(["success" => false, "message" => "Không thể xóa sản phẩm khỏi giỏ hàng"]);
    }
} else {
    error_log("Missing required fields: MaKhachHang = " . (isset($data->MaKhachHang) ? $data->MaKhachHang : "NOT SET") . 
              ", MaSanPham = " . (isset($data->MaSanPham) ? $data->MaSanPham : "NOT SET"));
    echo json_encode(["success" => false, "message" => "Thiếu MaKhachHang hoặc MaSanPham"]);
}
