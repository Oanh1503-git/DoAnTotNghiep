<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Kết nối CSDL và model
include_once('../../config/database.php');
include_once('../../model/sanpham.php');

$database = new Database();
$db = $database->Connect();

$sanpham = new SanPham($db);

// Nhận minPrice và maxPrice từ GET
$minPrice = isset($_GET['minPrice']) ? (int)$_GET['minPrice'] : 0;
$maxPrice = isset($_GET['maxPrice']) ? (int)$_GET['maxPrice'] : 0;


// Gọi hàm lấy sản phẩm theo khoảng giá
$result = $sanpham->getSanPhamTheoKhoangGia($minPrice, $maxPrice);

if ($result->rowCount() > 0) {
    $sanpham_arr = [];
    while ($row = $result->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $sanpham_arr[] = [
            "MaSanPham" => $MaSanPham,
            "TenSanPham" => $TenSanPham,
            "Gia" => $Gia,
            "MoTa" => $MoTa,
            "HinhAnh" => $DuongDan ?? null // tránh undefined nếu không có hình
        ];
    }
    echo json_encode([
        "success" => true,
        "data" => $sanpham_arr
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Không tìm thấy sản phẩm trong khoảng giá này"
    ]);
}
?>
