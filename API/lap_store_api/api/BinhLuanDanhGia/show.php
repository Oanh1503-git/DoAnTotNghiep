<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/binhluandanhgia.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp BinhLuanDanhGia với kết nối PDO
$binhluan = new BinhLuanDanhGia($conn);

// Lấy MaBinhLuan từ URL (phương thức GET)
$binhluan->MaBinhLuan = isset($_GET['id']) ? $_GET['id'] : die(json_encode(array('message' => 'MaBinhLuan not exist')));

// Lấy thông tin chi tiết bình luận đánh giá
$binhluan->GetBinhLuanDanhGiaById();

// Kiểm tra nếu bình luận tồn tại
if ($binhluan->MaBinhLuan) {
    // Tạo mảng phản hồi
    $binhluan_item = array(
        'MaBinhLuan' => $binhluan->MaBinhLuan,
        'MaKhachHang' => $binhluan->MaKhachHang,
        'MaSanPham' => $binhluan->MaSanPham,
        'MaDonHang' => $binhluan->MaDonHang,
        'SoSao' => $binhluan->SoSao,
        'NoiDung' => $binhluan->NoiDung,
        'NgayDanhGia' => $binhluan->NgayDanhGia,
        'TrangThai' => $binhluan->TrangThai
    );

    // Xuất dữ liệu dạng JSON
    echo json_encode($binhluan_item, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    // Nếu không tìm thấy
    echo json_encode(array('message' => 'binh luan not exist'));
}
?>
