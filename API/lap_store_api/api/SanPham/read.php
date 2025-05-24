<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect();

// Khởi tạo lớp SanPham với kết nối PDO
$sanpham = new SanPham($conn);

// Lấy tất cả sản phẩm
$getAllSanPham = $sanpham->GetAllSanPham();

$num = $getAllSanPham->rowCount();

if ($num > 0) {
    $sanpham_array = [];
    $sanpham_array['sanpham'] = [];

    while ($row = $getAllSanPham->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $sanpham_item = array(
            'MaSanPham' => $MaSanPham,
            'TenSanPham' => $TenSanPham,
            'MaLoaiSanPham' => $MaLoaiSanPham,
            'mathuonghieu' => $mathuonghieu, // Thêm trường thương hiệu
            'CPU' => $CPU,
            'RAM' => $RAM,
            'CardManHinh' => $CardManHinh,
            'SSD' => $SSD,
            'ManHinh' => $ManHinh,
            'MaMauSac' => $MaMauSac,
            'Gia' => $Gia,
            'SoLuong' => $SoLuong,
            'MoTa' => $MoTa,
            'HinhAnh' => $DuongDan,
            'TrangThai' => $TrangThai,
        );
        array_push($sanpham_array['sanpham'], $sanpham_item);
    }
    echo json_encode($sanpham_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
}
?>