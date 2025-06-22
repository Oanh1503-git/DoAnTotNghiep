<?php
class Khachhang{
    private $conn;
    private $table = "khachhang";
    //Thuoc tinh
    public $MaKhachHang;
    public $HoTen;
    public $GioiTinh;
    public $NgaySinh;
    public $Email;
    public $SoDienThoai;

    //connect db

    public $totalCustomers;
    public function __construct($database){
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllKhachHang() {
        $query = "SELECT * FROM khachhang ORDER BY MaKhachHang DESC"; 
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }
    public function GetKhachHangById() {
        $query = "SELECT * FROM khachhang WHERE MaKhachHang = ? LIMIT 1"; 
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1,$this->MaKhachHang);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $this->HoTen = $row['HoTen'];
        $this->GioiTinh = $row['GioiTinh'];
        $this->NgaySinh = $row['NgaySinh'];
        $this->Email = $row['Email'];
        $this->SoDienThoai = $row['SoDienThoai'];
    } 

public function AddKhachHang(){
    $query = "INSERT INTO khachhang 
        SET MaKhachHang = :MaKhachHang, HoTen = :HoTen, GioiTinh = :GioiTinh, NgaySinh = :NgaySinh, Email = :Email, SoDienThoai = :SoDienThoai";

    $stmt = $this->conn->prepare($query);

    // Làm sạch dữ liệu
    $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
    $this->HoTen = htmlspecialchars(strip_tags($this->HoTen));
    $this->GioiTinh = htmlspecialchars(strip_tags($this->GioiTinh));
    $this->NgaySinh = htmlspecialchars(strip_tags($this->NgaySinh));
    $this->Email = htmlspecialchars(strip_tags($this->Email));
    $this->SoDienThoai = htmlspecialchars(strip_tags($this->SoDienThoai));

    // Gán tham số
    $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
    $stmt->bindParam(':HoTen', $this->HoTen);
    $stmt->bindParam(':GioiTinh', $this->GioiTinh);
    $stmt->bindParam(':NgaySinh', $this->NgaySinh);
    $stmt->bindParam(':Email', $this->Email);
    $stmt->bindParam(':SoDienThoai', $this->SoDienThoai);

    if($stmt->execute()){
        return true;
    }
    printf("Error %s.\n", $stmt->error);
    return false;
}

public function generateCustomerCode() {
    // Truy vấn để lấy cả tổng số KH và mã lớn nhất hiện tại
    $query = "SELECT COUNT(*) AS total, MAX(MaKhachHang) AS max_code FROM {$this->table}";
    $stmt = $this->conn->prepare($query);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    $total = (int)$result['total'];

    // Xử lý mã lớn nhất
    $maxCode = $result['max_code']; // Ví dụ: "KH035"
    $nextNumber = 1;

    if ($maxCode && preg_match('/KH(\d+)/', $maxCode, $matches)) {
        $nextNumber = (int)$matches[1] + 1;
    }

    // Sinh mã mới theo format KH + số có 3 chữ số
    $newCode = 'KH' . str_pad($nextNumber, 3, '0', STR_PAD_LEFT);

    // ✅ (Tùy chọn) Log hoặc lưu số lượng nếu bạn cần dùng sau
    $this->totalCustomers = $total; // Nếu bạn muốn lưu lại trong object

    return $newCode;
}

    public function UpdateKhachHang(){
        $query = "UPDATE khachhang SET HoTen =:HoTen, GioiTinh =:GioiTinh, NgaySinh =:NgaySinh,  Email =:Email, SoDienThoai =:SoDienThoai  WHERE MaKhachHang=:MaKhachHang";

        $stmt = $this->conn->prepare($query);

        $this->HoTen = htmlspecialchars(strip_tags($this->HoTen));
        $this->GioiTinh = htmlspecialchars(strip_tags($this->GioiTinh));
        $this->NgaySinh = htmlspecialchars(strip_tags($this->NgaySinh));
        $this->Email = htmlspecialchars(strip_tags($this->Email));
        $this->SoDienThoai = htmlspecialchars(strip_tags($this->SoDienThoai));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));

        $stmt->bindParam(':HoTen',$this->HoTen);
        $stmt->bindParam(':GioiTinh',$this->GioiTinh);
        $stmt->bindParam(':NgaySinh',$this->NgaySinh);
        $stmt->bindParam(':Email',$this->Email);
        $stmt->bindParam(':SoDienThoai',$this->SoDienThoai);
        $stmt->bindParam(':MaKhachHang',$this->MaKhachHang);

        try {
            if ($stmt->execute()) {
                return true;
            } else {
                $error = $stmt->errorInfo();
                printf("Error: %s.\n", $error[2]);
                return false;
            }
        } catch (PDOException $e) {
            printf("Database error: %s.\n", $e->getMessage());
            return false;
        }
    }

    public function deleteKhachHang(){
        $query = "DELETE FROM khachhang WHERE MaKhachHang=:MaKhachHang";

        $stmt = $this->conn->prepare($query);

        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));

        $stmt->bindParam(':MaKhachHang',$this->MaKhachHang);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }
}
?>