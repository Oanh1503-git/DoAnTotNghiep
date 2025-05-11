<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/diachi.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp DiaChiKhachHang
$diachi = new DiaChi($conn);

// Kiểm tra và lấy giá trị MaKhachHang từ query string
$diachi->MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(["message" => "MaKhachHang không được cung cấp."]));

// Lấy danh sách địa chỉ theo MaKhachHang
$getDiaChiByMaKhachHang = $diachi->GetDiaChiByMaKhachHang();
$numDiaChiByKhachHang = $getDiaChiByMaKhachHang->rowCount();

if ($numDiaChiByKhachHang > 0) {
    $diachi_array = [];
    $diachi_array['diachi'] = [];

    while ($row = $getDiaChiByMaKhachHang->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $diachi_item = array(
            'MaDiaChi' => $MaDiaChi,
            'ThongTinDiaChi' => $ThongTinDiaChi,
            'MaKhachHang' => $MaKhachHang,
            'TenNguoiNhan' => $TenNguoiNhan,
            'SoDienThoai' => $SoDienThoai,
            'MacDinh' => $MacDinh,
        );
        array_push($diachi_array['diachi'], $diachi_item);
    }
    echo json_encode($diachi_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy địa chỉ nào"]);
}

?>
