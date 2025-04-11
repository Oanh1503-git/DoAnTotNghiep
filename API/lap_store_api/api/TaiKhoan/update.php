<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: PUT');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/taikhoan.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $taikhoan = new TaiKhoan($conn);

    $data = json_decode(file_get_contents("php://input"));

    $taikhoan->TenTaiKhoan = $data->TenTaiKhoan;
    $taikhoan->MaKhachHang = $data->MaKhachHang;
    $taikhoan->MatKhau = $data->MatKhau;

    if ($taikhoan->UpdateTaiKhoan()) {
        echo json_encode(array('message' => 'Tai Khoan Updated'));
    } else {
        echo json_encode(array('message' => 'Tai Khoan Not Updated'));
    }

?>