<?php
// Cấu hình header
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

// Kết nối đến database và model
include_once('../../config/database.php');
include_once('../../model/hoadonban.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDonBan với kết nối PDO
$hoadon = new HoaDonBan($conn);

// Lấy dữ liệu JSON được gửi từ client
$data = json_decode(file_get_contents("php://input"));

// Gán dữ liệu cho các thuộc tính của đối tượng
$hoadon->MaHoaDonBan = $data->MaHoaDonBan;
$hoadon->MaKhachHang = $data->MaKhachHang;
$hoadon->NgayDatHang = $data->NgayDatHang;
$hoadon->MaDiaChi = $data->MaDiaChi;
$hoadon->TongTien = $data->TongTien;
$hoadon->PhuongThucThanhToan = $data->PhuongThucThanhToan;
$hoadon->TrangThai = $data->TrangThai;

// Gọi hàm thêm mới hóa đơn bán
if ($hoadon->addHoaDonBan()) {
    // Nếu thêm thành công
    echo json_encode(array('message', 'HoaDonBan create.'));
} else {
    // Nếu thêm thất bại
    echo json_encode(array('message' , 'HoaDonBan not create'));
}
?>
