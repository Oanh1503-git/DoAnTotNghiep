<?php
    header('Access-Control-Allow-Origin: *');
    header('Content-Type: application/json');
    header('Access-Control-Allow-Methods: DELETE');
    header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');
    
    include_once('../../config/database.php');
    include_once('../../model/giohang.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp GioHang với kết nối PDO
    $giohang = new giohang($conn);

    // Lấy dữ liệu từ body request
    $data = json_decode(file_get_contents("php://input"));

    // Kiểm tra nếu có MaGioHang trong dữ liệu
    if(isset($data->MaGioHang)) {
        $giohang->MaGioHang = $data->MaGioHang;

        // Thực hiện xóa giỏ hàng
        if ($giohang->deleteGioHang()) {
            echo json_encode(array('message' => 'Gio Hang Deleted'));
        } else {
            echo json_encode(array('message' => 'Gio Hang Not Deleted'));
        }
    } else {
        // Trả về lỗi nếu không có MaGioHang trong yêu cầu
        echo json_encode(array('success' => false, 'message' => 'Thiếu MaGioHang'));
    }
?>
