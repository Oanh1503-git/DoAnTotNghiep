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

// Lấy MaSanPham từ query string
$sanpham->MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : die("Sản phẩm không tồn tại");

// Lấy thông tin sản phẩm theo MaSanPham
$sanpham->GetSanPhamById();

// Kiểm tra nếu sản phẩm tồn tại và trả về dữ liệu
if ($sanpham->TenSanPham) {
    $sanpham_item = array(
        'MaSanPham' => $sanpham->MaSanPham,
        'TenSanPham' => $sanpham->TenSanPham,
        'MaLoaiSanPham' => $sanpham->MaLoaiSanPham,
        'mathuonghieu' => $sanpham->mathuonghieu ?? 1,
        'CPU' => $sanpham->CPU,
        'RAM' => $sanpham->RAM,
        'CardManHinh' => $sanpham->CardManHinh,
        'SSD' => $sanpham->SSD,
        'ManHinh' => $sanpham->ManHinh,
        'MaMauSac' => $sanpham->MaMauSac,
        'Gia' => $sanpham->Gia,
        'GiamGia' => $sanpham->GiamGia ?? 0,
        'SoLuong' => $sanpham->SoLuong,
        'MoTa' => $sanpham->MoTa,
        'HinhAnh' => $sanpham->HinhAnh,
        'TrangThai' => $sanpham->TrangThai
    );
    echo json_encode($sanpham_item, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    // Trả về thông báo lỗi nếu không tìm thấy sản phẩm
    echo json_encode(array("message" => "Sản phẩm không tồn tại."));
}
?>
