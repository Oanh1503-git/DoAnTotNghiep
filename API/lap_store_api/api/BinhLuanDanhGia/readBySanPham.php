<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/BinhLuanDanhGia.php');

$database = new Database();
$db = $database->Connect();

$binhluan = new BinhLuanDanhGia($db);

$MaSanPham = isset($_GET['MaSanPham']) ? $_GET['MaSanPham'] : die(json_encode(["message" => "Missing MaSanPham parameter."]));
$binhluan->MaSanPham = $MaSanPham;

$stmt = $binhluan->getBySanPham();

if ($stmt !== false) {
    $binhluan_arr = ["binhluan" => []];
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $binhluan_item = [
            "MaBinhLuan" => $MaBinhLuan,
            "MaKhachHang" => $MaKhachHang,
            "MaSanPham" => $MaSanPham,
            "MaHoaDonBan" => $MaHoaDonBan,
            "SoSao" => $SoSao,
            "NoiDung" => $NoiDung,
            "NgayDanhGia" => $NgayDanhGia,
            "TrangThai" => $TrangThai
        ];
        array_push($binhluan_arr["binhluan"], $binhluan_item);
    }
    
    http_response_code(200);
    echo json_encode($binhluan_arr);
} else {
    http_response_code(500);
    echo json_encode(["message" => "Error retrieving comments."]);
}
?>