<?php
ini_set('display_errors', 0);
error_reporting(0);

header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/taikhoan.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect();

// Khởi tạo lớp TaiKhoan với kết nối PDO
$taikhoan = new TaiKhoan($conn);

// Nhận dữ liệu JSON
$data = json_decode(file_get_contents("php://input"));
$taikhoan->TenTaiKhoan = $data->TenTaiKhoan;
$taikhoan->MaKhachHang = $data->MaKhachHang;
$taikhoan->MatKhau = $data->MatKhau;
$taikhoan->LoaiTaiKhoan = $data->LoaiTaiKhoan;
$taikhoan->TrangThai = $data->TrangThai;

sleep(2); // Delay nếu cần

// Gọi trong try-catch để tránh lỗi JSON malformed
try {
    if ($taikhoan->AddTaiKhoan()) {
        echo json_encode([
            'success' => true,
            'message' => 'Tài khoản được tạo thành công'
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Tạo tài khoản thất bại'
        ]);
    }
} catch (PDOException $e) {
    echo json_encode([
        'success' => false,
        'message' => 'Lỗi hệ thống: ' . $e->getMessage()
    ]);
}
?>
