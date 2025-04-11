<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: PUT');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/mausac.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $mausac = new mausac($conn);

    $data = json_decode(file_get_contents("php://input"));

    $mausac->MaMauSac = $data->MaMauSac;
    $mausac->TenMauSac = $data->TenMauSac;

    if($mausac->UpdateMauSac()){
        echo json_encode(array('message','MauSac Updated'));
    }
    else{
        echo json_encode(array('message','MauSac Not Updated'));
    }

?>