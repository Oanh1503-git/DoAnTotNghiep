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

if (
    isset($data->MaKhachHang) &&
    isset($data->NgayDatHang) &&
    isset($data->MaDiaChi) &&
    isset($data->TongTien) &&
    isset($data->PhuongThucThanhToan) &&
    isset($data->TrangThai)
) {
    // Gán dữ liệu cho đối tượng
    $hoadon->MaKhachHang = $data->MaKhachHang;
    $hoadon->NgayDatHang = $data->NgayDatHang;
    $hoadon->MaDiaChi = $data->MaDiaChi;
    $hoadon->TongTien = $data->TongTien;
    $hoadon->PhuongThucThanhToan = $data->PhuongThucThanhToan;
    $hoadon->TrangThai = $data->TrangThai;

    // Thêm hóa đơn
    if ($hoadon->addHoaDon()) {
        // Lấy ID vừa thêm
        $maHoaDonMoi = $conn->lastInsertId();

        echo json_encode([
            'success' => true,
            'maHoaDon' => (int)$maHoaDonMoi,
            'message' => 'Tạo hóa đơn thành công.'
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Không thể tạo hóa đơn.'
        ]);
    }
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Thiếu dữ liệu đầu vào.'
    ]);
}
?>
