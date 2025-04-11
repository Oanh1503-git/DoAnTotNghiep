<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/mausac.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $mausac = new mausac($conn);

    $mausac->MaMauSac = isset($_GET['id']) ? $_GET['id'] : die();

    $mausac->GetMauSacById();


    $mausac_item = array(
        'MaMauSac'=> $mausac->MaMauSac,
        'TenMauSac'=> $mausac->TenMauSac,
    );

    print_r(json_encode($mausac_item,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
?>