<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/diachi.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp DiaChi
$diachi = new DiaChi($conn);

// Kiểm tra và lấy giá trị MaKhachHang và MacDinh từ query string
if (isset($_GET['MaKhachHang']) && isset($_GET['MacDinh'])) {
    $diachi->MaKhachHang = htmlspecialchars(strip_tags($_GET['MaKhachHang'])); // Lọc dữ liệu đầu vào
    $diachi->MacDinh = htmlspecialchars(strip_tags($_GET['MacDinh']));

    // Gọi phương thức lấy địa chỉ mặc định
    $getDiaChiMacDinh = $diachi->GetDiaChiMacDinh();
    $numDiaChiMacDinh = $getDiaChiMacDinh->rowCount();

    if ($numDiaChiMacDinh > 0) {
        // Lấy dòng đầu tiên
        $row = $getDiaChiMacDinh->fetch(PDO::FETCH_ASSOC);

        // Định dạng dữ liệu trả về
        $diachi_item = array(
            'MaDiaChi' => $row['MaDiaChi'],
            'ThongTinDiaChi' => $row['ThongTinDiaChi'],
            'MaKhachHang' => $row['MaKhachHang'],
            'TenNguoiNhan' => $row['TenNguoiNhan'],
            'SoDienThoai' => $row['SoDienThoai'],
            'MacDinh' => $row['MacDinh'],
        );

        // Trả về kết quả dạng JSON
        echo json_encode($diachi_item, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    } else {
        // Không tìm thấy dữ liệu
        echo json_encode(["message" => "Không tìm thấy địa chỉ mặc định nào"]);
    }
} else {
    // Trường hợp thiếu tham số đầu vào
    echo json_encode(["message" => "Thiếu tham số MaKhachHang hoặc MacDinh"]);
}
?>
