<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: PUT');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/diachi.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp DiaChi với kết nối PDO
    $diachi = new DiaChi($conn);

    // Nhận dữ liệu JSON từ request body
    $data = json_decode(file_get_contents("php://input"));

    if (!empty($data->MaKhachHang)) {
        $diachi->MaKhachHang = $data->MaKhachHang;

        if ($diachi->UpdateDiaChiMacDinh()) {
            echo json_encode(['message' => 'Đã cập nhật địa chỉ mặc định.']);
        } else {
            echo json_encode(['message' => 'Cập nhật địa chỉ thất bại.']);
        }
    } else {
        echo json_encode(['message' => 'Thiếu MaKhachHang']);
    }
?>
