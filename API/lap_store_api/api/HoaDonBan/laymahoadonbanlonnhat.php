<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hoadonban.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDonBan với kết nối PDO
$hoadon = new HoaDonBan($conn);

// Lấy giá trị MaHoaDonBan lớn nhất
$maxMaHoaDonBan = $hoadon->getMaxMaHoaDonBan();

if ($maxMaHoaDonBan !== null) {
    echo json_encode(["MaHoaDonBan" => $maxMaHoaDonBan], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy MaHoaDonBan lớn nhất."]);
}
?>
