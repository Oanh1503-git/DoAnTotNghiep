<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Lấy MaHoaDon từ URL (phương thức GET)
$hoadon->MaHoaDon = isset($_GET['MaHoaDon']) ? $_GET['MaHoaDon'] : die(json_encode(array('message' => 'Mã hóa đơn không tồn tại.')));

// Lấy thông tin chi tiết hóa đơn bán
$hoadon->GetHoaDonById();

// Kiểm tra nếu hóa đơn tồn tại
if ($hoadon->MaHoaDon) {
    // Tạo mảng phản hồi
    $hoadon_item = array(
        'MaHoaDon' => $hoadon->MaHoaDon,
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
    echo json_encode(array('message' => 'Hóa đơn  không tồn tại.'));
}
?>
