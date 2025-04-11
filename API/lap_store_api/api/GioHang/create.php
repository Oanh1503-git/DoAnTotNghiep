<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/giohang.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $giohang = new giohang($conn);

    $data = json_decode(file_get_contents("php://input"));
    $giohang->MaKhachHang = $data->MaKhachHang;
    $giohang->MaSanPham = $data->MaSanPham;
    $giohang->SoLuong = $data->SoLuong;
    $giohang->TrangThai = $data->TrangThai;


    if($giohang->AddGioHang()){
        echo json_encode(array('message','GioHang Created'));
    }
    else{
        echo json_encode(array('message','GioHang Not Created'));
    }

?>