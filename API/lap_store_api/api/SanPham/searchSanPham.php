<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp SanPham với kết nối PDO
$sanpham = new SanPham($conn);

// Kiểm tra và lấy giá trị search từ query string
$search= isset($_GET['search']) ? $_GET['search'] : die(json_encode(["message" => "Từ khóa tìm kiếm không được cung cấp."]));

// Lấy danh sách sản phẩm theo từ khóa tìm kiếm

// Kiểm tra nếu có sản phẩm
$stmt = $sanpham->SearchSanPham($search);
$num = $stmt->rowCount();

if ($num > 0) {
    $sanpham_array = [];
    $sanpham_array['sanpham'] = [];

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $sanpham_item = array(
            'MaSanPham' => (int)$MaSanPham,
            'TenSanPham' => $TenSanPham,
            'MaLoaiSanPham' => (int)$MaLoaiSanPham,
            'mathuonghieu' => (int)$mathuonghieu,
            'CPU' => $CPU,
            'RAM' => $RAM,
            'CardManHinh' => $CardManHinh,
            'SSD' => $SSD,
            'ManHinh' => $ManHinh,
            'MaMauSac' => (int)$MaMauSac,
            'Gia' => (int)$Gia,
            'SoLuong' => (int)$SoLuong,
            'MoTa' => $MoTa,
            'HinhAnh' => $DuongDan,
            'TrangThai' => (int)$TrangThai,
        );
        array_push($sanpham_array['sanpham'], $sanpham_item);
    }
    echo json_encode($sanpham_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(['sanpham' => []], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    echo json_encode(["message" => "Không tìm thấy sản phẩm nào với từ khóa tìm kiếm: " . $searchTerm]);
}
?>
