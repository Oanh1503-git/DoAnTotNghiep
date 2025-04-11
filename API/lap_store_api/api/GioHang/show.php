<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/giohang.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $giohang = new giohang($conn);

    $giohang->MaGioHang = isset($_GET['id']) ? $_GET['id'] : die();

    $giohang->GetGioHangById();


    $giohang_item = array(
        'MaGioHang'=> $giohang->MaGioHang,
        'MaKhachHang'=> $giohang->MaKhachHang,
        'MaSanPham'=> $giohang->MaSanPham,
        'SoLuong'=> $giohang->SoLuong,
        'TrangThai'=> $giohang->TrangThai
    );

    print_r(json_encode($giohang_item,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
?>