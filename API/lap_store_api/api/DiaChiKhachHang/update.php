<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/diachikhachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp DiaChiKhachHang với kết nối PDO
$diachikhachhang = new DiaChiKhachHang($conn);

// Lấy dữ liệu từ đầu vào
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra dữ liệu đầu vào
if (
    isset($data->MaKhachHang) && 
    isset($data->MaDiaChi) && 
    isset($data->GhiChu)
) {
    // Gán giá trị cho các thuộc tính
    $diachikhachhang->MaKhachHang = $data->MaKhachHang;
    $diachikhachhang->MaDiaChi = $data->MaDiaChi;
    $diachikhachhang->GhiChu = $data->GhiChu;

    // Gọi phương thức UpdateDiaChiKhachHang
    if ($diachikhachhang->UpdateDiaChiKhachHang()) {
        echo json_encode(array('message' => 'Dia Chi Khach Hang Updated'));
    } else {
        echo json_encode(array('message' => 'Dia Chi Khach Hang Not Updated'));
    }
} else {
    echo json_encode(array('message' => 'Incomplete data provided'));
}
?>
