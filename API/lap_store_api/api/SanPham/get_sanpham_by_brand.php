<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Kết nối CSDL
include_once '../config/database.php';
include_once '../models/SanPhamModel.php';

$database = new database();
$conn = $database->Connect();

$model = new SanPham($database);

// Kiểm tra phương thức và tham số truyền vào
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['brand'])) {
    $brand = $_GET['brand'];

    $stmt = $model->GetSanPhamByBrand($brand);
    $num = $stmt->rowCount();

    if ($num > 0) {
        $products = $stmt->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode([
            "status" => "success",
            "data" => $products
        ]);
    } else {
        echo json_encode([
            "status" => "empty",
            "message" => "Không tìm thấy sản phẩm theo thương hiệu '$brand'"
        ]);
    }
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Thiếu tham số brand hoặc sai phương thức (phải là GET)"
    ]);
}
