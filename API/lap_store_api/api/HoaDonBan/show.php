<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hoadonban.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDonBan với kết nối PDO
$hoadon = new HoaDonBan($conn);

// Lấy MaHoaDonBan từ URL (phương thức GET)
$hoadon->MaHoaDonBan = isset($_GET['MaHoaDonBan']) ? $_GET['MaHoaDonBan'] : die(json_encode(array('message' => 'Mã hóa đơn không tồn tại.')));

// Lấy thông tin chi tiết hóa đơn bán
$hoadon->GetHoaDonBanById();

// Kiểm tra nếu hóa đơn tồn tại
if ($hoadon->MaHoaDonBan) {
    // Tạo mảng phản hồi
    $hoadon_item = array(
        'MaHoaDonBan' => $hoadon->MaHoaDonBan,
        'MaKhachHang' => $hoadon->MaKhachHang,
        'NgayDatHang' => $hoadon->NgayDatHang,
        'MaDiaChi' => $hoadon->MaDiaChi,
        'TongTien' => $hoadon->TongTien,
        'PhuongThucThanhToan' => $hoadon->PhuongThucThanhToan,
        'TrangThai' => $hoadon->TrangThai
    );

    // Xuất dữ liệu dạng JSON
    echo json_encode($hoadon_item, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    // Nếu không tìm thấy
    echo json_encode(array('message' => 'Hóa đơn bán không tồn tại.'));
}
?>
