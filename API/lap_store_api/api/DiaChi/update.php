<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: PUT');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/diachi.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp diachi với kết nối PDO
    $diachi = new DiaChi($conn);

    $data = json_decode(file_get_contents("php://input"));

    $diachi->MaDiaChi = $data->MaDiaChi;
    $diachi->ThongTinDiaChi = $data->ThongTinDiaChi;
    $diachi->MaKhachHang = $data->MaKhachHang;
    $diachi->TenNguoiNhan = $data->TenNguoiNhan;
    $diachi->SoDienThoai = $data->SoDienThoai;
    $diachi->MacDinh = $data->MacDinh;

    if($diachi->UpdateDiaChi()){
        echo json_encode(array('message','Dia Chi Updated'));
    }
    else{
        echo json_encode(array('message','Dia Chi Not Updated'));
    }

?>