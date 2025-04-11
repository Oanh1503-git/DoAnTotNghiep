<?php
// Cấu hình header
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

// Kết nối đến database và model
include_once('../../config/database.php');
include_once('../../model/chitiethoadonban.php');  // Điều chỉnh theo lớp ChiTietHoaDonBan

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp ChiTietHoaDonBan với kết nối PDO
$chitiethoadonban = new ChiTietHoaDonBan($conn);

// Lấy dữ liệu JSON được gửi từ client
$data = json_decode(file_get_contents("php://input"));

// Gán dữ liệu cho các thuộc tính của đối tượng ChiTietHoaDonBan
$chitiethoadonban->MaSanPham = $data->MaSanPham;  // Mã sản phẩm
$chitiethoadonban->SoLuong = $data->SoLuong;  // Số lượng sản phẩm
$chitiethoadonban->DonGia = $data->DonGia;  // Đơn giá sản phẩm
$chitiethoadonban->ThanhTien = $data->ThanhTien;  // Thành tiền
$chitiethoadonban->GiamGia = $data->GiamGia;  // Giảm giá (nếu có)

// Trì hoãn 3 giây trước khi gọi phương thức addDetail
sleep(4);

// Gọi hàm thêm chi tiết hóa đơn bán
if ($chitiethoadonban->addDetail()) {
    // Nếu thêm thành công
    echo json_encode(array('message' => 'Chi tiết hóa đơn bán đã được thêm thành công.'));
} else {
    // Nếu thêm thất bại
    echo json_encode(array('message' => 'Không thể thêm chi tiết hóa đơn bán.'));
}
?>
