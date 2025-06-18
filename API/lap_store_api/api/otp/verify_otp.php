<?php
date_default_timezone_set('Asia/Ho_Chi_Minh');
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Tạm thời bật lỗi nếu bạn cần debug
// ini_set('display_errors', 1);
// error_reporting(E_ALL);

require_once(__DIR__ . '/../../config/database.php');
require_once(__DIR__ . '/../../model/otp.php');

// Đọc dữ liệu JSON từ frontend
$data = json_decode(file_get_contents("php://input"), true);

// Kiểm tra dữ liệu đầu vào
if (!isset($data['username']) || !isset($data['otp'])) {
    echo json_encode([
        "success" => false,
        "message" => "Thiếu username hoặc mã OTP"
    ]);
    exit;
}

$username = trim($data['username']);
$otpCode = trim($data['otp']);

// Kết nối database
$database = new Database();
$conn = $database->Connect();
if (!$conn) {
    echo json_encode([
        "success" => false,
        "message" => "Kết nối cơ sở dữ liệu thất bại"
    ]);
    exit;
}

// Tạo đối tượng OTP và gán giá trị
$otp = new Otp($conn);
$otp->TenTaiKhoan = $username;
$otp->otp_code = $otpCode;

// Xác minh OTP
if ($otp->verify()) {
    if ($otp->markAsUsed()) {
        echo json_encode([
            "success" => true,
            "message" => "Xác thực thành công"
        ]);
    } else {
        error_log("✅ verify OK nhưng ❌ markAsUsed FAILED: $username - $otpCode");
        echo json_encode([
            "success" => false,
            "message" => "Không thể đánh dấu OTP đã dùng. Vui lòng thử lại."
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Mã OTP không hợp lệ hoặc đã hết hạn"
    ]);
}
