<?php
// API: get_donhang_daydu.php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/HoaDon.php');

// Kết nối database
$database = new Database();
$db = $database->Connect();

// Tạo đối tượng model
$hoaDon = new HoaDon($db);

// Nhận tham số từ client
$MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(["message" => "Thiếu MaKhachHang"]));

// Gọi hàm model
$stmt = $hoaDon->getDonHangDayDuTheoKhachHang($MaKhachHang);

// Xử lý dữ liệu
if ($stmt) {
    $result = [];

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $maHD = $row['MaHoaDon'];

        if (!isset($result[$maHD])) {
            $result[$maHD] = [
                "MaHoaDon" => $maHD,
                "NgayDatHang" => $row['NgayDatHang'],
                "TongTien" => $row['TongTien'],
                "TrangThai" => $row['TrangThai'],
                "PhuongThucThanhToan" => $row['PhuongThucThanhToan'],
                "SanPham" => []
            ];
        }

        $result[$maHD]["SanPham"][] = [
            "TenSanPham" => $row['TenSanPham'],
            "Gia" => $row['Gia'],
            "SoLuong" => $row['SoLuong'],
            "ThanhTien" => $row['ThanhTien'],
            "HinhAnh" => $row['HinhAnh']
        ];
    }

if (count($result) > 0) {
    echo json_encode(array_values($result)); // ✅ Mảng nhiều đơn hàng
} else {
    echo json_encode([]); // ✅ Trả về mảng rỗng thay vì message
}
}
?>
