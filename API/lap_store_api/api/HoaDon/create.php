<?php
// Cấu hình header
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

// Kết nối đến database và model
include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Lấy dữ liệu JSON được gửi từ client
$data = json_decode(file_get_contents("php://input"));

// Gán dữ liệu cho các thuộc tính của đối tượng
$hoadon->MaHoaDon = $data->MaHoaDon;
$hoadon->MaKhachHang = $data->MaKhachHang;
$hoadon->NgayDatHang = $data->NgayDatHang;
$hoadon->MaDiaChi = $data->MaDiaChi;
$hoadon->TongTien = $data->TongTien;
$hoadon->PhuongThucThanhToan = $data->PhuongThucThanhToan;
$hoadon->TrangThai = $data->TrangThai;

// Gọi hàm thêm mới hóa đơn bán
if ($hoadon->addHoaDon()) {
    // Lấy mã hóa đơn vừa thêm (giả sử có hàm getMaxMaHoaDon)
    $maHoaDonMoi = $hoadon->getMaxMaHoaDon();
    echo json_encode(array(
        'success' => true,
        'message' => 'HoaDon create.',
        'maHoaDon' => $maHoaDonMoi
    ));
} else {
    echo json_encode(array(
        'success' => false,
        'message' => 'HoaDon not create'
    ));
}
?>
