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

// Khởi tạo lớp DiaChiKhachHang với kết nối PDO
$diachikhachhang = new DiaChiKhachHang($conn);

// Kiểm tra nếu có tham số MaKhachHang
if (isset($_GET['MaKhachHang'])) {
    $diachikhachhang->MaKhachHang = $_GET['MaKhachHang'];
    $getDiaChi = $diachikhachhang->GetDiaChiByMaKhachHang();
} else {
    $getDiaChi = $diachikhachhang->GetAllDiaChiKhachHang();
}

$num = $getDiaChi->rowCount();

if ($num > 0) {
    $diachi_array = [];
    $diachi_array['diachikhachhang'] = [];

    while ($row = $getDiaChi->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $diachi_item = array(
            'MaKhachHang' => $MaKhachHang,
            'MaDiaChi' => $MaDiaChi,
            'ThongTinDiaChi' => isset($ThongTinDiaChi) ? $ThongTinDiaChi : null, // Nếu kết hợp JOIN với bảng `diachi`
            'GhiChu' => $GhiChu,
        );
        array_push($diachi_array['diachikhachhang'], $diachi_item);
    }
    echo json_encode($diachi_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    echo json_encode(
        array('message' => 'No records found')
    );
}
?>
