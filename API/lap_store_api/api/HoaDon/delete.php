<?php
// Cấu hình header
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: DELETE');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

// Kết nối đến database và model
include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Lấy dữ liệu JSON từ yêu cầu
$data = json_decode(file_get_contents("php://input"));

// Gán giá trị cho thuộc tính MaHoaDon
$hoadon->MaHoaDon = $data->MaHoaDon;

// Gọi hàm deleteHoaDon() để xóa hóa đơn
if ($hoadon->deleteHoaDon()) {
    // Nếu xóa thành công
    echo json_encode(array('message' => 'Hóa đơn bán đã được xóa.'));
} else {
    // Nếu xóa thất bại
    echo json_encode(array('message' => 'Không thể xóa hóa đơn bán.'));
}
?>
