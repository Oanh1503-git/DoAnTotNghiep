<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/taikhoan.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp Khachhang với kết nối PDO
$taikhoan = new TaiKhoan($conn);

// Lấy tất cả khách hàng
$getAllTaiKhoan = $taikhoan->GetAllTaiKhoan();

$num = $getAllTaiKhoan->rowCount();

if($num>0){
    $taikhoan_array =[];
    $taikhoan_array['taikhoan'] =[];

    while($row = $getAllTaiKhoan->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $taikhoan_item = array(
            'TenTaiKhoan'=> $TenTaiKhoan,
            'MaKhachHang'=> $MaKhachHang,
            'MatKhau'=>$MatKhau,
            'LoaiTaiKhoan'=>$LoaiTaiKhoan,
            'TrangThai'=>$TrangThai
        );
        array_push($taikhoan_array['taikhoan'],$taikhoan_item);
    }
    echo json_encode($taikhoan_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>