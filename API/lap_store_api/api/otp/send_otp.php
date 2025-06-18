<?php
date_default_timezone_set('Asia/Ho_Chi_Minh'); 
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
ini_set('display_errors', 0);
error_reporting(0);

include_once(__DIR__ . '/../../config/database.php');
include_once(__DIR__ . '/../../model/otp.php');
require_once(__DIR__ . '/../../vendor/autoload.php');

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// 👉 Kết nối database
$database = new database();
$conn = $database->Connect();

$data = json_decode(file_get_contents("php://input"), true);

if (!isset($data['email']) || !isset($data['username'])) {
    echo json_encode(["success" => false, "message" => "Thiếu email hoặc username"]);
    exit;
}

$email = $data['email'];
$username = $data['username'];
$otpCode = str_pad(rand(0, 999999), 6, '0', STR_PAD_LEFT);
$expires = date('Y-m-d H:i:s', strtotime('+5 minutes'));

$otp = new Otp($conn);
$otp->TenTaiKhoan = $username;
$otp->otp_code = $otpCode;
$otp->expires_at = $expires;

if (!$otp->create()) {
    echo json_encode(["success" => false, "message" => "Không thể lưu OTP vào database"]);
    exit;
}

$mail = new PHPMailer(true);
try {
    $mail->isSMTP();
    $mail->Host       = 'smtp.gmail.com';
    $mail->SMTPAuth   = true;
    $mail->Username   = 'laptopstoredoan2025@gmail.com';
    $mail->Password   = 'npoyhyqkaebhmjqd'; // App password
    $mail->SMTPSecure = 'tls';
    $mail->Port       = 587;

    $mail->setFrom('laptopstoredoan2025@gmail.com', 'LaptopStore');
    $mail->addAddress($email);
    $mail->Subject = 'Mã xác thực OTP';
    $mail->Body    = "Xin chào $username,\n\nMã OTP của bạn là: $otpCode\nMã sẽ hết hạn sau 5 phút.";

    $mail->send();
    echo json_encode(["success" => true, "message" => "Đã gửi OTP về email."]);
} catch (Exception $e) {
    error_log("PHPMailer Error: " . $mail->ErrorInfo);
    echo json_encode(["success" => false, "message" => "Gửi mail thất bại: {$mail->ErrorInfo}"]);
}
