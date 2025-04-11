<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/chitiethoadonban.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp diachi với kết nối PDO
$chitiethoadonban = new ChiTietHoaDonBan($conn);

// Lấy tất cả diachi
$getAllchitiethoadonban = $chitiethoadonban->getAllChiTietHoaDon();

$num = $getAllchitiethoadonban->rowCount();

if($num>0){
    $chitiethoadonban_array =[];
    $chitiethoadonban_array['chitiethoadonban'] =[];

    while($row = $getAllchitiethoadonban->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $MaChiTietHoaDonBan = (int)$MaChiTietHoaDonBan;

        $chitiethoadonban_item = array(
            'MaChiTietHoaDonBan' => $MaChiTietHoaDonBan,
            'MaHoaDonBan' => $MaHoaDonBan,
            'MaSanPham' => $MaSanPham,
            'SoLuong' => $SoLuong,
            'DonGia' => $DonGia,
            'ThanhTien' => $ThanhTien,
            'GiamGia' => $GiamGia
        );
        array_push($chitiethoadonban_array['chitiethoadonban'],$chitiethoadonban_item);
    }
    echo json_encode($chitiethoadonban_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>