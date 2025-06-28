<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    // Cấu hình header
    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

    // Kết nối đến database và model
    include_once('../../config/database.php');
    include_once('../../model/binhluandanhgia.php');

    // Tạo đối tượng database và kết nối
    $database = new Database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp BinhLuanDanhGia với kết nối PDO
    $binhluan = new BinhLuanDanhGia($conn);

    // Lấy dữ liệu JSON được gửi từ client
    $data = json_decode(file_get_contents("php://input"));

    // Gán dữ liệu cho các thuộc tính của đối tượng
    $binhluan->MaBinhLuan = $data->MaBinhLuan;
    $binhluan->MaKhachHang = $data->MaKhachHang;
    $binhluan->MaSanPham = $data->MaSanPham;
    $binhluan->MaDonHang = $data->MaDonHang;
    $binhluan->SoSao = $data->SoSao;
    $binhluan->NoiDung = $data->NoiDung;
    $binhluan->NgayDanhGia = $data->NgayDanhGia;
    $binhluan->TrangThai = $data->TrangThai;

    // Gọi hàm thêm mới bình luận đánh giá
    if ($binhluan->AddBinhLuanDanhGia()) {
        // Nếu thêm thành công
        echo json_encode(array('message' => 'Binhluan create.'));
    } else {
        // Nếu thêm thất bại
        echo json_encode(array('message' => 'binhluan not create'));
    }
?>
