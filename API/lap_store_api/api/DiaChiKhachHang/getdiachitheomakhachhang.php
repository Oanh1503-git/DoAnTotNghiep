<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/diachikhachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp DiaChiKhachHang
$diachikhachhang = new DiaChiKhachHang($conn);

// Kiểm tra và lấy giá trị MaKhachHang từ query string
$diachikhachhang->MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(["message" => "MaKhachHang không được cung cấp."]));

// Lấy danh sách địa chỉ theo MaKhachHang
$getDiaChiByMaKhachHang = $diachikhachhang->GetDiaChiByMaKhachHang();
$numDiaChiByKhachHang = $getDiaChiByMaKhachHang->rowCount();

if ($numDiaChiByKhachHang > 0) {
    $diachi_array = [];
    $diachi_array['diachikhachhang'] = [];

    while ($row = $getDiaChiByMaKhachHang->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $diachi_item = array(
            'MaDiaChi' => $MaDiaChi,
            'MaKhachHang' => $MaKhachHang,
        );
        array_push($diachi_array['diachikhachhang'], $diachi_item);
    }
    echo json_encode($diachi_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy địa chỉ nào với MaKhachHang = " . $diachikhachhang->MaKhachHang]);
}

?>
