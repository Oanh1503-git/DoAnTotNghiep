<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/chitiethoadon.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp SanPham với kết nối PDO
$chitiethoadon = new ChiTietHoaDon($conn);

// Kiểm tra và lấy giá trị MaLoaiSanPham từ query string
$chitiethoadon->MaHoaDon = isset($_GET['MaHoaDon']) ? $_GET['MaHoaDon'] : die(json_encode(["message" => "MaHoaDon không được cung cấp."]));

// Lấy danh sách sản phẩm theo MaLoaiSanPham
$getchitiethoadonbymahoadon = $chitiethoadon->getChiTietHoaDonByMaHoaDon();
$numHoadonbyMaHoaDon = $getchitiethoadonbymahoadon->rowCount();

if ($numHoadonbyMaHoaDon > 0) {
    $HoadonbyMaHoaDon = [];
    $HoadonbyMaHoaDon['chitiethoadon'] = [];

    while ($row = $getchitiethoadonbymahoadon->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $chitiethoadon_item = array(
            'MaChiTietHoaDon' => $MaChiTietHoaDon,
            'MaHoaDon' => $MaHoaDon,
            'MaSanPham' => $MaSanPham,
            'SoLuong' => $SoLuong,
            'DonGia' => $DonGia,
            'ThanhTien' => $ThanhTien,
            'GiamGia' => $GiamGia
        );
        array_push($HoadonbyMaHoaDon['chitiethoadon'], $chitiethoadon_item);
    }
    echo json_encode($HoadonbyMaHoaDon, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy sản phẩm nào với MaHoaDon = " . $chitiethoadon->MaHoaDon]);
}
?>
