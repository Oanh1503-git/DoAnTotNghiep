<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Kiểm tra và lấy giá trị MaKhachHang và TrangThai từ query string
$hoadon->TrangThai = isset($_GET['TrangThai']) ? (int)$_GET['TrangThai'] : die(json_encode(["message" => "TrangThai không được cung cấp."]));

// Lấy danh sách hóa đơn theo MaKhachHang và TrangThai
$getHoaDonTheoTrangThai = $hoadon->getAllHoaDonTheoTrangThai();
$numHoaDonTheoTrangThai = $getHoaDonTheoTrangThai->rowCount();

if ($numHoaDonTheoTrangThai > 0) {
    $hoadonbykhachhang_array = [];
    $hoadonbykhachhang_array['hoadon'] = [];

    while ($row = $getHoaDonTheoTrangThai->fetch(PDO::FETCH_ASSOC)) {

        error_log("MaHoaDon: " . $row['MaHoaDon']);
        extract($row);
        
        // Ép kiểu TrangThai thành số nguyên
        $TrangThai = (int)$TrangThai; // Đảm bảo TrangThai là kiểu Int

        $hoadon_item = array(
            'MaHoaDon'=> $MaHoaDon,
            'MaKhachHang'=> $MaKhachHang,
            'NgayDatHang'=> $NgayDatHang,
            'MaDiaChi'=> $MaDiaChi,
            'TongTien'=> $TongTien,
            'PhuongThucThanhToan'=> $PhuongThucThanhToan,
            'TrangThai'=> $TrangThai, // Đảm bảo TrangThai là kiểu Int
        );
        array_push($hoadonbykhachhang_array['hoadon'], $hoadon_item);
    }

    // Trả về kết quả dưới dạng JSON
    echo json_encode($hoadonbykhachhang_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy hóa đơn nào với MaKhachHang = " . $hoadon->MaKhachHang . " và TrangThai = " . $hoadon->TrangThai]);
}
?>
