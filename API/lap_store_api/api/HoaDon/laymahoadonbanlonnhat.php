<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Lấy giá trị MaHoaDon lớn nhất
$maxMaHoaDon = $hoadon->getMaxMaHoaDon();

if ($maxMaHoaDon !== null) {
    echo json_encode(["MaHoaDon" => $maxMaHoaDon], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy MaHoaDon lớn nhất."]);
}
?>
