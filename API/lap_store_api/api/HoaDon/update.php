<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Lấy dữ liệu từ yêu cầu PUT
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra nếu dữ liệu đầu vào tồn tại
if (!isset($data->MaHoaDon)) {
    echo json_encode(array('message' => 'MaHoaDon exist.'));
    die();
}

// Gán dữ liệu từ yêu cầu PUT vào đối tượng
$hoadon->MaHoaDon = $data->MaHoaDon;
$hoadon->MaKhachHang = $data->MaKhachHang;
$hoadon->NgayDatHang = $data->NgayDatHang;
$hoadon->MaDiaChi = $data->MaDiaChi;
$hoadon->TongTien = $data->TongTien;
$hoadon->PhuongThucThanhToan = $data->PhuongThucThanhToan;
$hoadon->TrangThai = $data->TrangThai;

// Cập nhật hóa đơn bán
if ($hoadon->updateHoaDon()) {
    echo json_encode(array('message' => 'HoaDon update successful'));
} else {
    echo json_encode(array('message' => 'HoaDon failed to update'));
}
?>
