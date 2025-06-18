<?php
class Otp {
    private $conn;
    private $table = "otp";

    public $id;
    public $TenTaiKhoan;
    public $otp_code;
    public $created_at;
    public $expires_at;
    public $is_used;

    public function __construct($db) {
        $this->conn = $db;
    }

    // Thêm mã OTP mới
    public function create() {
        $query = "INSERT INTO " . $this->table . " (TenTaiKhoan, otp_code, expires_at) 
                  VALUES (:TenTaiKhoan, :otp_code, :expires_at)";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(':TenTaiKhoan', $this->TenTaiKhoan);
        $stmt->bindParam(':otp_code', $this->otp_code);
        $stmt->bindParam(':expires_at', $this->expires_at);
        return $stmt->execute();
    }

    // Kiểm tra mã OTP hợp lệ
public function verify() {
    $query = "SELECT * FROM " . $this->table . " 
              WHERE TenTaiKhoan = :TenTaiKhoan 
              AND otp_code = :otp_code 
              AND is_used = 0 
              AND expires_at > NOW()
              LIMIT 1";
    $stmt = $this->conn->prepare($query);
    $stmt->bindParam(':TenTaiKhoan', $this->TenTaiKhoan);
    $stmt->bindParam(':otp_code', $this->otp_code);
    $stmt->execute();
    return $stmt->rowCount() > 0;
}

    // Đánh dấu OTP đã dùng

 public function markAsUsed() {
    $query = "UPDATE " . $this->table . " 
              SET is_used = 1 
              WHERE LOWER(TenTaiKhoan) = LOWER(:TenTaiKhoan) 
              AND otp_code = :otp_code 
              AND is_used = 0 
              LIMIT 1";
              
    $stmt = $this->conn->prepare($query);
    $stmt->bindParam(':TenTaiKhoan', $this->TenTaiKhoan);
    $stmt->bindParam(':otp_code', $this->otp_code);
    $stmt->execute();

    if ($stmt->rowCount() === 0) {
        error_log("❌ markAsUsed failed: Không tìm thấy dòng để cập nhật. Username={$this->TenTaiKhoan}, OTP={$this->otp_code}");
    }

    return $stmt->rowCount() > 0;
}

  
}
?>
