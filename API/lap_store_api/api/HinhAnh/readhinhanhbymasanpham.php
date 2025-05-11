<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/hinhanh.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp SanPham với kết nối PDO
$hinhanh = new HinhAnh($conn);

// Kiểm tra và lấy giá trị MaLoaiSanPham từ query string
$hinhanh->MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : die(json_encode(["message" => "MaLoaiSanPham không được cung cấp."]));

// Lấy danh sách sản phẩm theo MaLoaiSanPham
$getHinhAnhBySanPham = $hinhanh->GetHinhAnhByMaSanPham();
$numHinhAnhBySanPham = $getHinhAnhBySanPham->rowCount();

if ($numHinhAnhBySanPham > 0) {
    $hinhanhBySanPham_array = [];
    $hinhanhBySanPham_array['hinhanh'] = [];

    while ($row = $getHinhAnhBySanPham->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $hinhanh_item = array(
            'MaHinhAnh'=> $MaHinhAnh,
            'DuongDan'=> $DuongDan,
            'MaSanPham'=> $MaSanPham,
        );
        array_push($hinhanhBySanPham_array['hinhanh'], $hinhanh_item);
    }
    echo json_encode($hinhanhBySanPham_array, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} else {
    echo json_encode(["message" => "Không tìm thấy sản phẩm nào với MaLoaiSanPham = " . $hinhanh->MaSanPham]);
}
?>
