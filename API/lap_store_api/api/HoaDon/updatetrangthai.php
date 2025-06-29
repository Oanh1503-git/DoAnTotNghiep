<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: PUT");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once '../config/database.php';
include_once '../model/HoaDon.php';

try {
    // Kết nối database
    $database = new Database();
    $db = $database->Connect();

    // Khởi tạo đối tượng HoaDon
    $hoadon = new HoaDon($db);

    // Nhận dữ liệu JSON từ client
    $data = json_decode(file_get_contents("php://input"));

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
    if ($hoadon->updateTrangThai()) {
        echo json_encode([
            "success" => true,
            "message" => "Cập nhật trạng thái thành công"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Cập nhật trạng thái thất bại"
        ]);
    }

} catch (Exception $e) {
    echo json_encode([
        "success" => false,
        "message" => "Lỗi server: " . $e->getMessage()
    ]);
}
?>
