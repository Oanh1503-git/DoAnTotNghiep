<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/khachhang.php');

// Kết nối database
$database = new Database();
$db = $database->Connect();

// Tạo đối tượng khách hàng
$khachhang = new Khachhang($db);

// Lấy mã khách hàng từ tham số GET
if (isset($_GET['maKhachHang'])) {
    $khachhang->MaKhachHang = $_GET['maKhachHang'];

    // Gọi hàm lấy thông tin
    $khachhang->GetKhachHangById();

    // Kiểm tra nếu có dữ liệu
    if ($khachhang->HoTen != null) {
        $response = [
            "success" => true,
            "data" => [
                "MaKhachHang" => $khachhang->MaKhachHang,
                "HoTen" => $khachhang->HoTen,
                "GioiTinh" => $khachhang->GioiTinh,
                "NgaySinh" => $khachhang->NgaySinh,
                "Email" => $khachhang->Email,
                "SoDienThoai" => $khachhang->SoDienThoai
            ]
        ];
    } else {
        $response = [
            "success" => false,
            "message" => "Không tìm thấy khách hàng"
        ];
    }
} else {
    $response = [
        "success" => false,
        "message" => "Thiếu mã khách hàng"
    ];
}

// Trả về kết quả JSON
echo json_encode($response);
