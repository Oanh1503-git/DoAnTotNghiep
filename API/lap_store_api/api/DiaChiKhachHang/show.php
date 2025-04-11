<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/diachikhachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp DiaChiKhachHang với kết nối PDO
$diachikhachhang = new DiaChiKhachHang($conn);

// Lấy MaKhachHang và MaDiaChi từ URL
$diachikhachhang->MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die('Missing MaKhachHang');
$diachikhachhang->MaDiaChi = isset($_GET['MaDiaChi']) ? $_GET['MaDiaChi'] : die('Missing MaDiaChi');

// Lấy thông tin chi tiết địa chỉ khách hàng
$getDiaChi = $diachikhachhang->GetDiaChiByMaKhachHang();
$row = $getDiaChi->fetch(PDO::FETCH_ASSOC);

if ($row) {
    $diachikhachhang_item = array(
        'MaKhachHang' => $row['MaKhachHang'],
        'MaDiaChi' => $row['MaDiaChi'],
        'ThongTinDiaChi' => isset($row['ThongTinDiaChi']) ? $row['ThongTinDiaChi'] : null,
        'GhiChu' => $row['GhiChu']
    );

    echo json_encode($diachikhachhang_item, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    echo json_encode(
        array('message' => 'No record found')
    );
}
?>
