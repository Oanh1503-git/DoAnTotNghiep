<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

// Kết nối tới database và các model cần thiết
include_once('../../config/database.php');
include_once('../../model/giohang.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp GioHang với kết nối PDO
$giohang = new GioHang($conn);

// Kiểm tra và lấy giá trị MaKhachHang từ query string
if (!isset($_GET['MaKhachHang']) || empty($_GET['MaKhachHang'])) {
    http_response_code(400); // Bad Request
    echo json_encode([
        "giohang" => [],
        "message" => "MaKhachHang không được cung cấp hoặc rỗng."
    ], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    exit;
}
$giohang->MaKhachHang = $_GET['MaKhachHang'];

// Lấy danh sách giỏ hàng theo MaKhachHang
try {
    $getGioHangByMaKhachHang = $giohang->GetGioHangByMaKhachHang();
    $numGioHangByKhachHang = $getGioHangByMaKhachHang->rowCount();

    if ($numGioHangByKhachHang > 0) {
        $giohangbykhachhang_array = [];
        $giohangbykhachhang_array['giohang'] = [];

        while ($row = $getGioHangByMaKhachHang->fetch(PDO::FETCH_ASSOC)) {
            extract($row);

            $giohang_item = [
                'MaGioHang'   => $MaGioHang,
                'MaKhachHang' => $MaKhachHang,
                'MaSanPham'   => $MaSanPham,
                'SoLuong'     => $SoLuong,
                'TrangThai'   => $TrangThai,
                'TenSanPham'  => $TenSanPham,
                'Gia'         => $Gia,
                'HinhAnh'     => $HinhAnh ?? '' // Đảm bảo không trả về null
            ];
            array_push($giohangbykhachhang_array['giohang'], $giohang_item);
        }

        // Trả về kết quả dưới dạng JSON
        http_response_code(200); // OK
        echo json_encode($giohangbykhachhang_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    } else {
        http_response_code(200); // OK, nhưng không có dữ liệu
        echo json_encode([
            "giohang" => [],
            "message" => "Không tìm thấy sản phẩm trong giỏ hàng cho MaKhachHang = " . $giohang->MaKhachHang
        ], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    }
} catch (Exception $e) {
    http_response_code(500); // Internal Server Error
    echo json_encode([
        "giohang" => [],
        "message" => "Lỗi khi lấy giỏ hàng: " . $e->getMessage()
    ], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
}
?>