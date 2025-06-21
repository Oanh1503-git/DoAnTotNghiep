<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

include_once('../../config/database.php');
include_once('../../model/hoadon.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp HoaDon với kết nối PDO
$hoadon = new HoaDon($conn);

// Lấy tất cả dữ liệu hóa đơn bán
$getAllHoaDon = $hoadon->getAllHoaDon();
$num = $getAllHoaDon->rowCount();

// Kiểm tra nếu có dữ liệu
if ($num > 0) {
    $hoadon_array = [];
    $hoadon_array['hoadon'] = [];

    while ($row = $getAllHoaDon->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $hoadon_item = array(
            'MaHoaDon' => $MaHoaDon,
            'MaKhachHang' => $MaKhachHang,
            'NgayDatHang' => $NgayDatHang,
            'MaDiaChi' => $MaDiaChi,
            'TongTien' => $TongTien,
            'PhuongThucThanhToan' => $PhuongThucThanhToan,
            'TrangThai' => $TrangThai
        );

        array_push($hoadon_array['hoadon'], $hoadon_item);
    }

    // Xuất dữ liệu dạng JSON
    echo json_encode($hoadon_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    // Trường hợp không có dữ liệu
    echo json_encode(
        array('message' => 'Không có hóa đơn bán nào.')
    );
}
?>
