<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: PUT');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

    include_once('../../config/database.php');
    include_once('../../model/binhluandanhgia.php');

    // Tạo đối tượng database và kết nối
    $database = new Database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp BinhLuanDanhGia với kết nối PDO
    $binhluan = new BinhLuanDanhGia($conn);

    // Lấy dữ liệu từ yêu cầu PUT
    $data = json_decode(file_get_contents("php://input"));

    // Kiểm tra nếu dữ liệu đầu vào tồn tại
    if (!isset($data->MaBinhLuan)) {
        echo json_encode(array('message' => 'MaBinhLuan not exist'));
        die();
    }

    // Gán dữ liệu từ yêu cầu PUT vào đối tượng
    $binhluan->MaBinhLuan = $data->MaBinhLuan;
    $binhluan->MaKhachHang = $data->MaKhachHang;
    $binhluan->MaSanPham = $data->MaSanPham;
    $binhluan->MaHoaDon = $data->MaHoaDon;
    $binhluan->SoSao = $data->SoSao;
    $binhluan->NoiDung = $data->NoiDung;
    $binhluan->NgayDanhGia = $data->NgayDanhGia;
    $binhluan->TrangThai = $data->TrangThai;

    // Cập nhật bình luận đánh giá
    if ($binhluan->UpdateBinhLuanDanhGia()) {
        echo json_encode(array('message' => 'BinhLuan update.'));
    } else {
        echo json_encode(array('message' => 'BinhLUan not update.'));
    }
?>
