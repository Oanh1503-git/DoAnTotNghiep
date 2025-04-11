<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

include_once('../../config/database.php');
include_once('../../model/binhluandanhgia.php');

// Tạo đối tượng database và kết nối
$database = new Database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp BinhLuanDanhGia với kết nối PDO
$binhluan = new BinhLuanDanhGia($conn);

// Lấy tất cả dữ liệu bình luận đánh giá
$getAllBinhLuan = $binhluan->GetAllBinhLuanDanhGia();
$num = $getAllBinhLuan->rowCount();

// Kiểm tra nếu có dữ liệu
if ($num > 0) {
    $binhluan_array = [];
    $binhluan_array['binhluandanhgia'] = [];

    while ($row = $getAllBinhLuan->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $binhluan_item = array(
            'MaBinhLuan' => $MaBinhLuan,
            'MaKhachHang' => $MaKhachHang,
            'MaSanPham' => $MaSanPham,
            'MaDonHang' => $MaDonHang,
            'SoSao' => $SoSao,
            'NoiDung' => $NoiDung,
            'NgayDanhGia' => $NgayDanhGia,
            'TrangThai' => $TrangThai
        );

        array_push($binhluan_array['binhluandanhgia'], $binhluan_item);
    }

    // Xuất dữ liệu dạng JSON
    echo json_encode($binhluan_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    // Trường hợp không có dữ liệu
    echo json_encode(
        array('message' => 'Not BinhLuan ')
    );
}
?>
