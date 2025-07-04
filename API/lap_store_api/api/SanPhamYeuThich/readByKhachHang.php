<?php
// SanPhamYeuThich/readByKhachHang.php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once('../../config/database.php');
include_once('../../model/SanPhamYeuThich.php');

$database = new Database();
$db = $database->Connect();

$yeuthich = new SanPhamYeuThich($db);

$MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die(json_encode(array("message" => "Missing MaKhachHang parameter.")));

try {
    $stmt = $yeuthich->readByKhachHang($MaKhachHang);
    $num = $stmt->rowCount();

    $yeuthich_arr = array();
    $yeuthich_arr["hiensanphamyeuthich"] = array();

    if ($num > 0) {
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $yeuthich_item = array(
                "id" => $row['id'],
                "MaSanPham" => $row['MaSanPham'],
                "MaKhachHang" => $row['MaKhachHang'],
                "TenSanPham" => $row['TenSanPham'],
                "Gia" => $row['Gia'],
                "HinhAnh" => $row['HinhAnh']
            );
            array_push($yeuthich_arr["hiensanphamyeuthich"], $yeuthich_item);
        }
        http_response_code(200);
        echo json_encode($yeuthich_arr);
    } else {
    http_response_code(200);
    echo json_encode(array("hiensanphamyeuthich" => []));
}

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(array("message" => "Database error: " . $e->getMessage()));
}
?>
