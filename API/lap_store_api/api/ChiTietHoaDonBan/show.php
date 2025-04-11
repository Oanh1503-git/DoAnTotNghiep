<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/chitiethoadonban.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp ChiTietHoaDonBan với kết nối PDO
    $chitiethoadonban = new ChiTietHoaDonBan($conn);

    // Kiểm tra và ép kiểu MaChiTietHoaDonBan về int
    $chitiethoadonban->MaChiTietHoaDonBan = isset($_GET['MaChiTietHoaDonBan']) ? (int) $_GET['MaChiTietHoaDonBan'] : die();

    // Lấy chi tiết theo MaChiTietHoaDonBan
    $chitiethoadonban->getDetailById();

    // Chuyển kết quả thành mảng và encode JSON
    $chitiethoadonban_item = array(
        'MaChiTietHoaDonBan' => $chitiethoadonban->MaChiTietHoaDonBan,
        'MaHoaDonBan' => $chitiethoadonban->MaHoaDonBan,
        'MaSanPham' => $chitiethoadonban->MaSanPham,
        'SoLuong' => $chitiethoadonban->SoLuong,
        'DonGia' => $chitiethoadonban->DonGia,
        'ThanhTien' => $chitiethoadonban->ThanhTien,
        'GiamGia' => $chitiethoadonban->GiamGia
    );

    // Trả về kết quả dưới dạng JSON
    echo json_encode($chitiethoadonban_item, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>
