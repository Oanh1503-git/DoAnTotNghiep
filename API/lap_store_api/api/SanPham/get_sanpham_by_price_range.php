<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Kết nối database
$database = new Database();
$conn = $database->Connect();

$sanpham = new SanPham($conn);

// Nhận giá min và max từ query string
$minPrice = isset($_GET['min']) ? (int)$_GET['min'] : 0;
$maxPrice = isset($_GET['max']) ? (int)$_GET['max'] : 100000000; // mặc định tối đa

$stmt = $sanpham->getSanPhamByPriceRange($minPrice, $maxPrice);

if ($stmt->rowCount() > 0) {
    $arr = [];
    $arr['sanpham'] = [];

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $item = [
            'MaSanPham'=> $MaSanPham,
            'TenSanPham'=> $TenSanPham,
            'MaLoaiSanPham'=> $MaLoaiSanPham,
            'CPU'=> $CPU,
            'RAM'=> $RAM,
            'CardManHinh'=> $CardManHinh,
            'SSD'=> $SSD,
            'ManHinh'=> $ManHinh,
            'MaMauSac'=> $MaMauSac,
            'Gia'=> $Gia,
            'SoLuong'=> $SoLuong,
            'MoTa'=> $MoTa,
            'HinhAnh'=> $DuongDan,
            'TrangThai'=> $TrangThai
        ];
        $arr['sanpham'][] = $item;
    }

    echo json_encode($arr, JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["message" => "Không có sản phẩm trong khoảng giá này"]);
}
