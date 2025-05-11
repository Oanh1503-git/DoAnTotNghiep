<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp SanPham với kết nối PDO
$sanpham = new SanPham($conn);

// Kiểm tra và lấy giá trị MaLoaiSanPham từ query string
$sanpham->MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(["message" => "MaLoaiSanPham không được cung cấp."]));

// Lấy danh sách sản phẩm theo MaLoaiSanPham
$getSanPhamByGioHang = $sanpham->GetSanPhamByGioHang();
$numSanPhamByGioHang = $getSanPhamByGioHang->rowCount();

if ($numSanPhamByGioHang > 0) {
    $sanphamByGioHang_array = [];
    $sanphamByGioHang_array['sanpham'] = [];

    while ($row = $getSanPhamByGioHang->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $sanpham_item = array(
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
            'TrangThai'=> $TrangThai,
        );
        array_push($sanphamByGioHang_array['sanpham'], $sanpham_item);
    }
    echo json_encode($sanphamByGioHang_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy sản phẩm nào với MaLoaiSanPham = " . $sanpham->MaLoaiSanPham]);
}
?>
