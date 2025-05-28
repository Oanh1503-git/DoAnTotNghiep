<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDonBan với kết nối PDO
$sanpham = new SanPham($conn);

// Kiểm tra và lấy giá trị MaKhachHang và TrangThai từ query string
$sanpham->MaLoaiSanPham = isset($_GET['MaLoaiSanPham']) ? (int)$_GET['MaLoaiSanPham'] : die(json_encode(["message" => "TrangThai không được cung cấp."]));

// Lấy danh sách hóa đơn theo MaKhachHang và TrangThai
$getSanPhamTheoLoai= $sanpham->GetSanPhamByLoai();
$numSanPhamTheoLoai = $getSanPhamTheoLoai->rowCount();

if ($numSanPhamTheoLoai > 0) {
    $sanphamtheoloai_array = [];
    $sanphamtheoloai_array['sanpham'] = [];

    while ($row = $getSanPhamTheoLoai->fetch(PDO::FETCH_ASSOC)) {

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
        array_push($sanphamtheoloai_array['sanpham'], $sanpham_item);
    }

    // Trả về kết quả dưới dạng JSON
    echo json_encode($sanphamtheoloai_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy hóa đơn nào với MaKhachHang = " . $hoadon->MaKhachHang . " và TrangThai = " . $hoadon->TrangThai]);
}
?>
