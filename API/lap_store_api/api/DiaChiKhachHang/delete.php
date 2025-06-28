<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: DELETE');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/diachikhachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp DiaChiKhachHang với kết nối PDO
$diachikhachhang = new DiaChiKhachHang($conn);

// Lấy dữ liệu đầu vào
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra dữ liệu đầu vào
if (isset($data->MaKhachHang) && isset($data->MaDiaChi)) {
    // Gán giá trị cho thuộc tính
    $diachikhachhang->MaKhachHang = $data->MaKhachHang;
    $diachikhachhang->MaDiaChi = $data->MaDiaChi;

    // Gọi phương thức xóa
    if ($diachikhachhang->DeleteDiaChiKhachHang()) {
        echo json_encode(array('message' => 'Dia Chi Khach Hang Deleted'));
    } else {
        echo json_encode(array('message' => 'Dia Chi Khach Hang Not Deleted'));
    }
} else {
    echo json_encode(array('message' => 'Incomplete data provided'));
}
?>
