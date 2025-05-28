<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

include_once '../../config/database.php';
include_once '../../model/sanpham.php';

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect();

if (!$conn) {
    http_response_code(500);
    echo json_encode(array("message" => "Lỗi kết nối cơ sở dữ liệu."), JSON_UNESCAPED_UNICODE);
    exit;
}

// Khởi tạo lớp SanPham với kết nối PDO
$sanpham = new SanPham($conn);

// Lấy và kiểm tra MaSanPham từ query string
if (!isset($_GET['MaSanPham']) || !is_numeric($_GET['MaSanPham'])) {
    http_response_code(400);
    echo json_encode(array("message" => "MaSanPham không hợp lệ."), JSON_UNESCAPED_UNICODE);
    exit;
}

$sanpham->MaSanPham = (int)$_GET['MaSanPham'];

// Lấy thông tin sản phẩm theo MaSanPham
if ($sanpham->GetSanPhamById()) {
    $sanpham_item = array(
        'MaSanPham' => $sanpham->MaSanPham,
        'TenSanPham' => $sanpham->TenSanPham,
        'MaLoaiSanPham' => $sanpham->MaLoaiSanPham,
        'mathuonghieu' => $sanpham->mathuonghieu,
        'CPU' => $sanpham->CPU,
        'RAM' => $sanpham->RAM,
        'CardManHinh' => $sanpham->CardManHinh,
        'SSD' => $sanpham->SSD,
        'ManHinh' => $sanpham->ManHinh,
        'MaMauSac' => $sanpham->MaMauSac,
        'Gia' => $sanpham->Gia,
        'SoLuong' => $sanpham->SoLuong,
        'MoTa' => $sanpham->MoTa,
        'HinhAnh' => $sanpham->HinhAnh,
        'TrangThai' => $sanpham->TrangThai,
    );
    http_response_code(200);
    echo json_encode($sanpham_item, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    http_response_code(404);
    echo json_encode(array("message" => "Sản phẩm không tồn tại."), JSON_UNESCAPED_UNICODE);
}
?>