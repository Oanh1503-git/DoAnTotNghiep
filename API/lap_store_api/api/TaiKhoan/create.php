<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: POST');
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
    $taikhoan->LoaiTaiKhoan = $data->LoaiTaiKhoan;
    $taikhoan->TrangThai = $data->TrangThai;

    sleep(2);
    if($taikhoan->AddTaiKhoan()){
        echo json_encode(array('message','Tai Khoan Created'));
    }
    else{
        echo json_encode(array('message','Tai Khoan Not Created'));
    }

?>