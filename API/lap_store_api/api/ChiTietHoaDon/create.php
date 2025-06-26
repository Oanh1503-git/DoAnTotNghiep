<?php
// Nhận dữ liệu từ body JSON
$data = json_decode(file_get_contents("php://input"), true);

if (
    isset($data["MaHoaDon"]) &&
    isset($data["MaSanPham"]) &&
    isset($data["SoLuong"]) &&
    isset($data["DonGia"]) &&
    isset($data["GiamGia"]) &&
    isset($data["ThanhTien"])
) {
    // Kết nối DB
    include_once '../config/database.php';
    $db = (new Database())->Connect();

    $query = "INSERT INTO chitiet_hoadon (MaHoaDon, MaSanPham, SoLuong, DonGia, GiamGia, ThanhTien)
              VALUES (:MaHoaDon, :MaSanPham, :SoLuong, :DonGia, :GiamGia, :ThanhTien)";
    $stmt = $db->prepare($query);
    $stmt->bindParam(':MaHoaDon', $data['MaHoaDon']);
    $stmt->bindParam(':MaSanPham', $data['MaSanPham']);
    $stmt->bindParam(':SoLuong', $data['SoLuong']);
    $stmt->bindParam(':DonGia', $data['DonGia']);
    $stmt->bindParam(':GiamGia', $data['GiamGia']);
    $stmt->bindParam(':ThanhTien', $data['ThanhTien']);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Thêm chi tiết hóa đơn thành công"]);
    } else {
        echo json_encode(["success" => false, "message" => "Lỗi khi thêm chi tiết hóa đơn"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Thiếu dữ liệu"]);
}
?>
