<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/chitiethoadonban.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp SanPham với kết nối PDO
$chitiethoadon = new ChiTietHoaDonBan($conn);

// Kiểm tra và lấy giá trị MaLoaiSanPham từ query string
$chitiethoadon->MaHoaDonBan = isset($_GET['MaHoaDonBan']) ? $_GET['MaHoaDonBan'] : die(json_encode(["message" => "MaHoaDonBan không được cung cấp."]));

// Lấy danh sách sản phẩm theo MaLoaiSanPham
$getchitiethoadonbymahoadonban = $chitiethoadon->getChiTietHoaDonByMaHoaDonBan();
$numHoadonbyMaHoaDon = $getchitiethoadonbymahoadonban->rowCount();

if ($numHoadonbyMaHoaDon > 0) {
    $HoadonbyMaHoaDon = [];
    $HoadonbyMaHoaDon['chitiethoadonban'] = [];

    while ($row = $getchitiethoadonbymahoadonban->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $chitiethoadonban_item = array(
            'MaChiTietHoaDonBan' => $MaChiTietHoaDonBan,
            'MaHoaDonBan' => $MaHoaDonBan,
            'MaSanPham' => $MaSanPham,
            'SoLuong' => $SoLuong,
            'DonGia' => $DonGia,
            'ThanhTien' => $ThanhTien,
            'GiamGia' => $GiamGia
        );
        array_push($HoadonbyMaHoaDon['chitiethoadonban'], $chitiethoadonban_item);
    }
    echo json_encode($HoadonbyMaHoaDon, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy sản phẩm nào với MaHoaDonBan = " . $chitiethoadon->MaHoaDonBan]);
}
?>
