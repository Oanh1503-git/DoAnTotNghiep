<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
include_once '../../config/database.php';

$db = (new Database())->Connect();
$data = json_decode(file_get_contents("php://input"), true);

if (
    isset($data["MaHoaDon"]) &&
    isset($data["MaSanPham"]) &&
    isset($data["SoLuong"]) &&
    isset($data["DonGia"]) &&
    isset($data["GiamGia"])
) {
    $query = "INSERT INTO chitiethoadon (MaHoaDon, MaSanPham, SoLuong, DonGia, GiamGia)
              VALUES (:MaHoaDon, :MaSanPham, :SoLuong, :DonGia, :GiamGia)";
    $stmt = $db->prepare($query);

    $stmt->bindParam(':MaHoaDon', $data["MaHoaDon"]);
    $stmt->bindParam(':MaSanPham', $data["MaSanPham"]);
    $stmt->bindParam(':SoLuong', $data["SoLuong"]);
    $stmt->bindParam(':DonGia', $data["DonGia"]);
    $stmt->bindParam(':GiamGia', $data["GiamGia"]);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Thêm chi tiết hóa đơn thành công"]);
    } else {
        $error = $stmt->errorInfo();
        echo json_encode(["success" => false, "message" => "Thêm thất bại: " . $error[2]]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Thiếu dữ liệu"]);
}
?>
