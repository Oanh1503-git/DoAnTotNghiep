<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp GioHang với kết nối PDO
$giohang = new giohang($conn);

// Kiểm tra và lấy giá trị MaKhachHang từ query string
$giohang->MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(["message" => "MaKhachHang không được cung cấp."]));

// Lấy danh sách giỏ hàng theo MaKhachHang
$getGioHangByMaKhachHang = $giohang->GetGioHangByMaKhachHang();
$numGioHangByKhachHang = $getGioHangByMaKhachHang->rowCount();

if ($numGioHangByKhachHang > 0) {
    $giohangbykhachhang_array = [];
    $giohangbykhachhang_array['giohang'] = [];

    while ($row = $getGioHangByMaKhachHang->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $giohang_item = array(
            'MaGioHang'=> $MaGioHang,
            'MaKhachHang'=> $MaKhachHang,
            'MaSanPham'=> $MaSanPham,
            'SoLuong'=> $SoLuong,
            'TrangThai'=> $TrangThai,
        );
        array_push($giohangbykhachhang_array['giohang'], $giohang_item);
    }

    // Trả về kết quả dưới dạng JSON
    echo json_encode($giohangbykhachhang_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy sản phẩm nào với MaKhachHang = " . $giohang->MaKhachHang]);
}
?>
