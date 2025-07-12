<?php
// API: get_donhang_daydu.php

// Cấu hình header cho phép truy cập từ bên ngoài
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

// Import database và model
include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Khởi tạo kết nối database
$database = new Database();
$db = $database->Connect();

// Tạo đối tượng model HoaDon
$hoaDon = new HoaDon($db);

// Nhận MaKhachHang từ GET
$MaKhachHang = $_GET['MaKhachHang'] ?? null;

if (!$MaKhachHang) {
    echo json_encode(["success" => false, "message" => "Thiếu MaKhachHang"]);
    exit;
}

// Gọi hàm lấy dữ liệu từ model
$stmt = $hoaDon->getDonHangDayDuTheoKhachHang($MaKhachHang);

if ($stmt && $stmt->rowCount() > 0) {
    $result = [];

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $maHD = $row['MaHoaDon'];

        // Nếu hóa đơn chưa tồn tại trong mảng kết quả, khởi tạo
        if (!isset($result[$maHD])) {
            $result[$maHD] = [
                "MaHoaDon" => $row['MaHoaDon'],
                "NgayDatHang" => $row['NgayDatHang'],
                "NgayGiaoHang" => $row['NgayGiaoHang'],
                "TongTien" => $row['TongTien'],
                "TrangThai" => $row['TrangThai'],
                "PhuongThucThanhToan" => $row['PhuongThucThanhToan'],
                "SanPham" => []
            ];
        }

        // Thêm sản phẩm vào mảng SanPham của hóa đơn
        $result[$maHD]["SanPham"][] = [
            "MaSanPham" => $row['MaSanPham'],
            "TenSanPham" => $row['TenSanPham'],
            "Gia" => $row['Gia'],
            "SoLuong" => $row['SoLuong'],
            "ThanhTien" => $row['ThanhTien'],
            "HinhAnh" => $row['HinhAnh']
        ];
    }

    // Trả về dữ liệu
    echo json_encode(["success" => true, "data" => array_values($result)], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["success" => false, "message" => "Không có dữ liệu"], JSON_UNESCAPED_UNICODE);
}
?>
