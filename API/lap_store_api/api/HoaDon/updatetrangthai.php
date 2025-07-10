<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Kết nối database
$database = new Database();
$conn = $database->Connect();

// Khởi tạo đối tượng HoaDon
$hoadon = new HoaDon($conn);

// Nhận dữ liệu JSON từ client
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra dữ liệu đầu vào
if (!isset($data->MaHoaDon) || !isset($data->TrangThai)) {
    echo json_encode([
        "success" => false,
        "message" => "Thiếu dữ liệu MaHoaDon hoặc TrangThai"
    ]);
    exit;
}

// Gán dữ liệu vào đối tượng
$hoadon->MaHoaDon = $data->MaHoaDon;
$hoadon->TrangThai = $data->TrangThai;

// Gọi hàm updateTrangThai trong model
try {
    if ($hoadon->updateTrangThai()) {
        echo json_encode([
            "success" => true,
            "message" => "Cập nhật trạng thái thành công"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Cập nhật trạng thái thất bại hoặc không có hóa đơn nào được cập nhật"
        ]);
    }
} catch (Exception $e) {
    // Hiển thị lỗi SQL chi tiết nếu có exception
    echo json_encode([
        "success" => false,
        "message" => "Lỗi server: " . $e->getMessage()
    ]);
}
?>
