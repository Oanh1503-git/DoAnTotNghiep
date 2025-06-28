<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/chitiethoadon.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp ChiTietHoaDon với kết nối PDO
    $chitiethoadon = new ChiTietHoaDon($conn);

    // Kiểm tra và ép kiểu MaChiTietHoaDon về int
    $chitiethoadon->MaChiTietHoaDon = isset($_GET['MaChiTietHoaDon']) ? (int) $_GET['MaChiTietHoaDon'] : die();

    // Lấy chi tiết theo MaChiTietHoaDon
    $chitiethoadon->getDetailById();

    // Chuyển kết quả thành mảng và encode JSON
    $chitiethoadon_item = array(
        'MaChiTietHoaDon' => $chitiethoadon->MaChiTietHoaDon,
        'MaHoaDon' => $chitiethoadon->MaHoaDon,
        'MaSanPham' => $chitiethoadon->MaSanPham,
        'SoLuong' => $chitiethoadon->SoLuong,
        'DonGia' => $chitiethoadon->DonGia,
        'ThanhTien' => $chitiethoadon->ThanhTien,
        'GiamGia' => $chitiethoadon->GiamGia
    );

    // Trả về kết quả dưới dạng JSON
    echo json_encode($chitiethoadon_item, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>
