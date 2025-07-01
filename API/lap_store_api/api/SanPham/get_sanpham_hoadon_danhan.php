<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once("../../config/database.php");
$database = new Database();
$db = $database->Connect();

$maKhachHang = isset($_GET['maKhachHang']) ? $_GET['maKhachHang'] : die(json_encode(["message" => "Thiếu maKhachHang"]));

// Truy vấn sản phẩm trong hóa đơn có trạng thái = 4
$query = "
    SELECT 
        sp.MaSanPham,
        sp.TenSanPham,
        sp.Gia,
        ha.DuongDan AS HinhAnh,
        cthd.SoLuong,
        cthd.DonGia,
        cthd.GiamGia,
        cthd.ThanhTien,
        hd.MaHoaDon,
        hd.NgayDatHang
    FROM hoadon hd
    JOIN chitiethoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon
    JOIN sanpham sp ON cthd.MaSanPham = sp.MaSanPham
    LEFT JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham AND ha.MacDinh = 1
    WHERE hd.MaKhachHang = :maKhachHang AND hd.TrangThai = 4
    ORDER BY hd.NgayDatHang DESC
";

$stmt = $db->prepare($query);
$stmt->bindParam(":maKhachHang", $maKhachHang);
$stmt->execute();

$result = [];

while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $result[] = [
        "MaHoaDon" => $row["MaHoaDon"],
        "NgayDatHang" => $row["NgayDatHang"],
        "MaSanPham" => $row["MaSanPham"],
        "TenSanPham" => $row["TenSanPham"],
        "Gia" => $row["Gia"],
        "HinhAnh" => $row["HinhAnh"],
        "SoLuong" => $row["SoLuong"],
        "DonGia" => $row["DonGia"],
        "GiamGia" => $row["GiamGia"],
        "ThanhTien" => $row["ThanhTien"]
    ];
}

echo json_encode(["sanpham" => $result]);
?>
