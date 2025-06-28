<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/diachi.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $diachi = new DiaChi($conn);

    $diachi->MaDiaChi = isset($_GET['id']) ? $_GET['id'] : die();

    $diachi->GetDiaChiById();


    $diachi_item = array(
        'MaDiaChi'=> $diachi->MaDiaChi,
        'ThongTinDiaChi'=> $diachi->ThongTinDiaChi,
        'TenNguoiNhan'=> $diachi->TenNguoiNhan,
        'SoDienThoai'=> $diachi->SoDienThoai,
        'MacDinh'=> $diachi->MacDinh,
    );

    print_r(json_encode($diachi_item,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
?>