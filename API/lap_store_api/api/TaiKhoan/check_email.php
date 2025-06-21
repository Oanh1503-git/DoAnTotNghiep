<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

require_once(__DIR__ . '/../../config/database.php');
require_once(__DIR__ . '/../../model/taikhoan.php');

$data = json_decode(file_get_contents("php://input"), true);

// Kiểm tra đầu vào
if (!isset($data['email']) || empty(trim($data['email']))) {
    echo json_encode([
        "success" => false,
        "message" => "Thiếu hoặc rỗng trường email"
    ]);
    exit;
}

$email = trim($data['email']);

// Kết nối database
$conn = (new Database())->Connect();

// Gọi model TaiKhoan
$taikhoan = new TaiKhoan($conn);
$taikhoan->Email = $email;

if ($taikhoan->findUsernameByEmail()) {
    echo json_encode([
        "success" => true,
        "username" => $taikhoan->TenTaiKhoan
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Email không tồn tại trong hệ thống"
    ]);
}
