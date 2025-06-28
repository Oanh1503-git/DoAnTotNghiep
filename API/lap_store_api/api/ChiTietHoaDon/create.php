<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

// Nhận dữ liệu từ body JSON
$data = json_decode(file_get_contents("php://input"), true);

    include_once __DIR__ . '/../../config/database.php';
    $db = (new Database())->Connect();
// Kiểm tra dữ liệu đầu vào
if (
    isset($data["MaHoaDon"]) &&
    isset($data["MaSanPham"]) &&
    isset($data["SoLuong"]) &&
    isset($data["DonGia"]) &&
    isset($data["GiamGia"]) &&
    isset($data["ThanhTien"])
) {
    // Kết nối DB


    try {
        $query = "INSERT INTO chitiethoadon (MaHoaDon, MaSanPham, SoLuong, DonGia, GiamGia, ThanhTien)
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
            $error = $stmt->errorInfo();
            echo json_encode(["success" => false, "message" => "Lỗi khi thêm chi tiết hóa đơn", "error" => $error[2]]);
        }
    } catch (PDOException $e) {
        echo json_encode(["success" => false, "message" => "Lỗi PDO: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Thiếu dữ liệu"]);
}
