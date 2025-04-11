<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/loaisanpham.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $loaisanpham = new loaisanpham($conn);

    $loaisanpham->MaLoai = isset($_GET['id']) ? $_GET['id'] : die();

    $loaisanpham->GetLoaiSanPhamById();


    $loaisanpham_item = array(
        'MaLoai'=> $loaisanpham->MaLoai,
        'TenLoai'=> $loaisanpham->TenLoai,
    );

    print_r(json_encode($loaisanpham_item,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
?>