<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/khachhang.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo đối tượng Khachhang
$khachhang = new Khachhang($conn);

// Lấy dữ liệu JSON từ phía client
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra dữ liệu đầu vào
if (
    empty($data->HoTen) || empty($data->Email) || empty($data->SoDienThoai) || empty($data->MaDiaChi)
) {
    echo json_encode([
        "success" => false,
        "message" => "Vui lòng điền đầy đủ thông tin bắt buộc (HoTen, Email, SoDienThoai, MaDiaChi)"
    ]);
    exit;
}

// Gán thông tin cho đối tượng
$khachhang->MaKhachHang = $khachhang->generateCustomerCode();
$khachhang->HoTen = $data->HoTen;
$khachhang->Email = $data->Email;
$khachhang->SoDienThoai = $data->SoDienThoai;
$khachhang->MaDiaChi = $data->MaDiaChi;
$khachhang->GioiTinh = $data->GioiTinh ?? "Không rõ";
$khachhang->NgaySinh = $data->NgaySinh ?? null;

// Thêm khách hàng vào database
if ($khachhang->AddKhachHang()) {
    echo json_encode([
        "success" => true,
        "message" => "Khách hàng được tạo thành công",
        "ma_khach_hang" => $khachhang->MaKhachHang
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Tạo khách hàng thất bại"
    ]);
}
?>
