<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/binhluandanhgia.php');

// Kết nối database
$database = new Database();
$db = $database->Connect();

// Khởi tạo model
$binhluan = new binhluandanhgia($db);

// Nhận MaSanPham từ GET
$MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : null;

if ($MaSanPham === null) {
    echo json_encode(["success" => false, "message" => "Thiếu MaSanPham"]);
    exit;
}

// Gọi hàm model
$stmt = $binhluan->getDanhGiaByMaSanPham($MaSanPham);

if ($stmt && $stmt->rowCount() > 0) {
    $result = [];

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $result[] = [
            "MaBinhLuan" => $row['MaBinhLuan'],
            "MaKhachHang" => $row['MaKhachHang'],
            "TenKhachHang" => $row['TenKhachHang'],
            "MaSanPham" => $row['MaSanPham'],
            "MaHoaDon" => $row['MaHoaDon'],
            "SoSao" => $row['SoSao'],
            "NoiDung" => $row['NoiDung'],
            "NgayDanhGia" => $row['NgayDanhGia'],
            "TrangThai" => $row['TrangThai']
        ];
    }

    echo json_encode(["success" => true, "data" => $result], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["success" => false, "message" => "Không có đánh giá"], JSON_UNESCAPED_UNICODE);
}
?>
