<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

// Kết nối tới database và các model cần thiết
include_once('../../config/database.php');
include_once('../../model/sanpham.php');      // Đảm bảo đã include class SanPham
include_once('../../model/giohang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp GioHang với kết nối PDO
$giohang = new giohang($conn);

// Kiểm tra và lấy giá trị MaKhachHang từ query string
if (!isset($_GET['MaKhachHang'])) {
    echo json_encode([
        "giohang" => [],
        "message" => "MaKhachHang không được cung cấp."
    ]);
    exit;
}
$giohang->MaKhachHang = $_GET['MaKhachHang'];


// Lấy danh sách giỏ hàng theo MaKhachHang
$getGioHangByMaKhachHang = $giohang->GetGioHangByMaKhachHang();
$numGioHangByKhachHang = $getGioHangByMaKhachHang->rowCount();

if ($numGioHangByKhachHang > 0) {
    $giohangbykhachhang_array = [];
    $giohangbykhachhang_array['giohang'] = [];

    while ($row = $getGioHangByMaKhachHang->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        // Lấy thông tin sản phẩm chi tiết (nếu cần)
        $sanpham = new SanPham($conn);
        $sanpham->MaSanPham = $MaSanPham;
        $sanphamInfo = $sanpham->getSanPhamByMaSanPham();
        $sanphamData = $sanphamInfo ? $sanphamInfo->fetch(PDO::FETCH_ASSOC) : null;

        $giohang_item = [
            'MaGioHang'   => $MaGioHang,
            'MaKhachHang' => $MaKhachHang,
            'MaSanPham'   => $MaSanPham,
            'SoLuong'     => $SoLuong,
            'TrangThai'   => $TrangThai,
            'SanPham'     => $sanphamData // thêm thông tin sản phẩm (nếu muốn)
        ];
        array_push($giohangbykhachhang_array['giohang'], $giohang_item);
    }

    // Trả về kết quả dưới dạng JSON
    echo json_encode($giohangbykhachhang_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
  echo json_encode([
    "giohang" => [],
    "message" => "Không tìm thấy sản phẩm nào với MaKhachHang = " . $giohang->MaKhachHang
]);

}
?>