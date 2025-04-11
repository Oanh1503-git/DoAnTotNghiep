<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/khachhang.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $khachhang = new Khachhang($conn);

    $khachhang->MaKhachHang = isset($_GET['id']) ? $_GET['id'] : die();

    $khachhang->GetKhachHangById();


    $khachhang_item = array(
        'MaKhachHang'=> $khachhang->MaKhachHang,
        'HoTen'=> $khachhang->HoTen,
        'GioiTinh'=> $khachhang->GioiTinh,
        'NgaySinh'=> $khachhang->NgaySinh,
        'Email'=> $khachhang->Email,
        'SoDienThoai'=> $khachhang->SoDienThoai,
    );

    print_r(json_encode($khachhang_item,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
?>