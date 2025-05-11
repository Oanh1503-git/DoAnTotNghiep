<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/taikhoan.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp TaiKhoan với kết nối PDO
$taikhoan = new TaiKhoan($conn);

if (isset($_GET['tentaikhoan']) && isset($_GET['matkhau'])) {
    $taikhoan->TenTaiKhoan = htmlspecialchars(strip_tags($_GET['tentaikhoan'])); // Lọc dữ liệu đầu vào
    $taikhoan->MatKhau = htmlspecialchars(strip_tags($_GET['matkhau']));         // Lọc dữ liệu đầu vào

    // Kiểm tra đăng nhập
    if ($taikhoan->KiemTraDangNhap()) {
        // Đăng nhập thành công
        echo json_encode(
            array('result' => true),
            JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT
        );
    } else {
        // Đăng nhập thất bại
        echo json_encode(
            array('result' => false),
            JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT
        );
    }
    
} else {
    // Thiếu thông tin đăng nhập
    echo json_encode(
        array('result' => false, 'message' => 'Vui lòng cung cấp TenTaiKhoan và MatKhau.'),
        JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT
    );
}
?>
