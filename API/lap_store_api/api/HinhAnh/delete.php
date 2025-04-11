<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: DELETE');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/hinhanh.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $hinhanh = new HinhAnh($conn);

    $data = json_decode(file_get_contents("php://input"));

    $hinhanh->MaHinhAnh = $data->MaHinhAnh;

    if($hinhanh->deleteHinhAnh()){
        echo json_encode(array('message','Hinh Anh Deleted'));
    }
    else{
        echo json_encode(array('message','Hinh Anh Not Deleted'));
    }

?>