<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: DELETE');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/diachi.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $diachi = new DiaChi($conn);

    $data = json_decode(file_get_contents("php://input"));

    $diachi->MaDiaChi = $data->MaDiaChi;

    if($diachi->deleteDiaChi()){
        echo json_encode(array('message','Dia Chi Deleted'));
    }
    else{
        echo json_encode(array('message','Dia Chi Not Deleted'));
    }

?>