<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: PUT');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/sanpham.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Khachhang với kết nối PDO
    $sanpham = new SanPham($conn);

    $data = json_decode(file_get_contents("php://input"));

    $sanpham->MaSanPham = $data->MaSanPham;
    $sanpham->TenSanPham = $data->TenSanPham;
    $sanpham->MaLoaiSanPham = $data->MaLoaiSanPham;
    $sanpham->CPU = $data->CPU;
    $sanpham->RAM = $data->RAM;
    $sanpham->CardManHinh = $data->CardManHinh;
    $sanpham->SSD = $data->SSD;
    $sanpham->ManHinh = $data->ManHinh;
    $sanpham->MaMauSac = $data->MaMauSac;
    $sanpham->Gia = $data->Gia;
    $sanpham->SoLuong = $data->SoLuong;
    $sanpham->MoTa = $data->MoTa;
    $sanpham->TrangThai = $data->TrangThai;


    if($sanpham->UpdateSanPham()){
        echo json_encode(array('message','San Phamg Updated'));
    }
    else{
        echo json_encode(array('message','San Phamg Not Updated'));
    }

?>