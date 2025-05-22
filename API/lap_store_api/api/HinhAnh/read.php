<?php
header('Access-Control-Allow-Origin: *'); // Cho phép truy cập từ mọi nguồn
header('Content-Type: application/json'); // Định dạng trả về JSON

// Include file cấu hình và model
include_once('../../config/database.php');
include_once('../../model/hinhanh.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect();

// Khởi tạo lớp HinhAnh với kết nối PDO
$hinhanh = new HinhAnh($conn);

// Lấy tất cả hình ảnh
$stmt = $hinhanh->GetAllHinhAnh(); // Giả định hàm này tồn tại trong model

$num = $stmt->rowCount();

if ($num > 0) {
    $hinhanh_array = [];
    $hinhanh_array['hinhanh'] = [];

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $hinhanh_item = array(
            'MaHinhAnh' => (int)$MaHinhAnh,
            'DuongDan' => $DuongDan,
            'MaSanPham' => (int)$MaSanPham,
            'MacDinh' => (int)$MacDinh // Đảm bảo MacDinh là kiểu int
        );

        array_push($hinhanh_array['hinhanh'], $hinhanh_item);
    }

    // Trả về JSON với định dạng đúng
    echo json_encode($hinhanh_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    // Trả về mảng rỗng nếu không có dữ liệu
    echo json_encode(['hinhanh' => []], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
}
?>