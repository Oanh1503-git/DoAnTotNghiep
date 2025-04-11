<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/hinhanh.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $hinhanh = new HinhAnh($conn);

    $hinhanh->MaHinhAnh = isset($_GET['id']) ? $_GET['id'] : die();

    $hinhanh->GetHinhAnhById();


    $hinhanh_item = array(
        'MaHinhAnh'=> $hinhanh->MaHinhAnh,
        'DuongDan'=> $hinhanh->DuongDan,
        'MaSanPham'=> $hinhanh->MaSanPham,
    );

    print_r(json_encode($hinhanh_item,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
?>