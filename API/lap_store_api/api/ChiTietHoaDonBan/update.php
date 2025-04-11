<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/hoadonban.php');
include_once('../../model/chitiethoadonban.php'); // Lớp chi tiết hóa đơn

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDonBan với kết nối PDO
$hoadon = new HoaDonBan($conn);

// Khởi tạo lớp ChiTietHoaDonBan với kết nối PDO
$chitiet = new ChiTietHoaDonBan($conn);

// Lấy dữ liệu từ yêu cầu PUT
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra nếu dữ liệu đầu vào tồn tại
if (!isset($data->MaHoaDonBan)) {
    echo json_encode(array('message' => 'Mã hóa đơn không tồn tại.'));
    die();
}

// Gán dữ liệu từ yêu cầu PUT vào đối tượng HoaDonBan
$hoadon->MaHoaDonBan = $data->MaHoaDonBan;
$hoadon->MaKhachHang = $data->MaKhachHang;
$hoadon->NgayDatHang = $data->NgayDatHang;
$hoadon->MaDiaChi = $data->MaDiaChi;
$hoadon->TongTien = $data->TongTien;
$hoadon->TrangThai = $data->TrangThai;

// Cập nhật hóa đơn bán
if ($hoadon->updateHoaDonBan()) {
    // Nếu cập nhật hóa đơn bán thành công, tiếp tục cập nhật chi tiết hóa đơn
    if (isset($data->ChiTietHoaDon) && is_array($data->ChiTietHoaDon)) {
        // Xóa tất cả chi tiết hóa đơn cũ trước khi thêm chi tiết mới
        if ($chitiet->deleteDetail($hoadon->MaHoaDonBan)) {  // Sửa ở đây: gọi thuộc tính, không phải phương thức
            // Thêm chi tiết hóa đơn mới
            foreach ($data->ChiTietHoaDon as $detail) {
                $chitiet->MaHoaDonBan = $hoadon->MaHoaDonBan;
                $chitiet->MaSanPham = $detail->MaSanPham;
                $chitiet->SoLuong = $detail->SoLuong;
                $chitiet->DonGia = $detail->DonGia;
                $chitiet->ThanhTien = $detail->ThanhTien;
                $chitiet->GiamGia = $detail->GiamGia;

                // Thêm chi tiết vào cơ sở dữ liệu
                if (!$chitiet->addDetail()) {
                    echo json_encode(array('message' => 'Không thể cập nhật chi tiết hóa đơn.'));
                    die();
                }
            }
        }
    }
    echo json_encode(array('message' => 'Hóa đơn bán và chi tiết hóa đơn đã được cập nhật.'));
} else {
    echo json_encode(array('message' => 'Không thể cập nhật hóa đơn bán.'));
}
?>
