<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: DELETE');
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

    if($mausac->deleteMauSac()){
        echo json_encode(array('message','MauSac Deleted'));
    }
    else{
        echo json_encode(array('message','MauSac Not Deleted'));
    }

?>