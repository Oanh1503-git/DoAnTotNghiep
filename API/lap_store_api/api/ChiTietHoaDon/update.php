<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

// Thiết lập header
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

// Kết nối DB và model
include_once('../../config/database.php');
include_once('../../model/hoadon.php');
include_once('../../model/chitiethoadon.php');

// Kết nối DB
$database = new Database();
$conn = $database->Connect();

// Khởi tạo đối tượng
$hoadon = new HoaDon($conn);
$chitiet = new ChiTietHoaDon($conn);

// Lấy JSON từ body
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra bắt buộc có MaHoaDon
if (!isset($data->MaHoaDon)) {
    echo json_encode(['success' => false, 'message' => 'Thiếu Mã hóa đơn']);
    http_response_code(400);
    exit;
}

// Gán dữ liệu hóa đơn
$hoadon->MaHoaDon = $data->MaHoaDon;
$hoadon->MaKhachHang = $data->MaKhachHang ?? null;
$hoadon->NgayDatHang = $data->NgayDatHang ?? null;
$hoadon->MaDiaChi = $data->MaDiaChi ?? null;
$hoadon->TongTien = $data->TongTien ?? null;
$hoadon->TrangThai = $data->TrangThai ?? 0;

// Cập nhật hóa đơn
if ($hoadon->updateHoaDon()) {

    // Nếu có danh sách chi tiết hóa đơn truyền vào
    if (isset($data->ChiTietHoaDon) && is_array($data->ChiTietHoaDon)) {

        // Xóa tất cả chi tiết cũ
        if (!$chitiet->deleteDetail($hoadon->MaHoaDon)) {
            echo json_encode(['success' => false, 'message' => 'Không thể xóa chi tiết cũ']);
            http_response_code(500);
            exit;
        }

        // Thêm từng chi tiết mới
        foreach ($data->ChiTietHoaDon as $item) {
            $chitiet->MaHoaDon = $hoadon->MaHoaDon;
            $chitiet->MaSanPham = $item->MaSanPham ?? null;
            $chitiet->SoLuong = $item->SoLuong ?? 0;
            $chitiet->DonGia = $item->DonGia ?? 0;
            $chitiet->GiamGia = $item->GiamGia ?? 0;
            $chitiet->ThanhTien = $item->ThanhTien ?? 0;

            if (!$chitiet->addDetail()) {
                echo json_encode(['success' => false, 'message' => 'Lỗi thêm chi tiết hóa đơn']);
                http_response_code(500);
                exit;
            }
        }
    }

    // Thành công
    echo json_encode(['success' => true, 'message' => 'Cập nhật hóa đơn thành công']);
} else {
    echo json_encode(['success' => false, 'message' => 'Cập nhật hóa đơn thất bại']);
    http_response_code(500);
}
?>
