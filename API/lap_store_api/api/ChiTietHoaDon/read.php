<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/chitiethoadon.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp diachi với kết nối PDO
$chitiethoadon = new ChiTietHoaDon($conn);

// Lấy tất cả diachi
$getAllchitiethoadon = $chitiethoadon->getAllChiTietHoaDon();

$num = $getAllchitiethoadon->rowCount();

if($num>0){
    $chitiethoadon_array =[];
    $chitiethoadon_array['chitiethoadon'] =[];

    while($row = $getAllchitiethoadon->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $MaChiTietHoaDon = (int)$MaChiTietHoaDon;

        $chitiethoadon_item = array(
            'MaChiTietHoaDon' => $MaChiTietHoaDon,
            'MaHoaDon' => $MaHoaDon,
            'MaSanPham' => $MaSanPham,
            'SoLuong' => $SoLuong,
            'DonGia' => $DonGia,
            'ThanhTien' => $ThanhTien,
            'GiamGia' => $GiamGia
        );
        array_push($chitiethoadon_array['chitiethoadon'],$chitiethoadon_item);
    }
    echo json_encode($chitiethoadon_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>