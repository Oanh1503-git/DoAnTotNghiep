<?php
class Khachhang{
    private $conn;

    //Thuoc tinh
    public $MaKhachHang;
    public $HoTen;
    public $GioiTinh;
    public $NgaySinh;
    public $Email;
    public $SoDienThoai;

    //connect db

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

    public function generateCustomerCode() {
        // Lấy số lượng khách hàng hiện tại
        $query = "SELECT COUNT(*) as total FROM khachhang";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $customerCount = $row['total'] + 1;

        // Lấy thông tin thời gian hiện tại
        $currentMonth = date('m');
        $currentDayOfWeek = date('D'); // Thu in English (Mon, Tue, Wed, etc.)
        $currentMinute = date('i');

        // Lấy 2 chữ cái đầu của thứ trong tiếng Anh
        $dayPrefix = substr($currentDayOfWeek, 0, 2);

        // Tạo mã khách hàng
        $customerCode = sprintf("KH%03d%02d%s%02d", 
            $customerCount,
            $currentMonth,
            $dayPrefix,
            $currentMinute
        );

        return $customerCode;
    }

    public function AddKhachHang(){
        // Tạo mã khách hàng mới
        $this->MaKhachHang = $this->generateCustomerCode();

        $query = "INSERT INTO khachhang SET MaKhachHang =:MaKhachHang, HoTen =:HoTen, GioiTinh =:GioiTinh, NgaySinh =:NgaySinh, Email =:Email, SoDienThoai =:SoDienThoai";

        $stmt = $this->conn->prepare($query);

        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->HoTen = htmlspecialchars(strip_tags($this->HoTen));
        $this->GioiTinh = htmlspecialchars(strip_tags($this->GioiTinh));
        $this->NgaySinh = htmlspecialchars(strip_tags($this->NgaySinh));
        $this->Email = htmlspecialchars(strip_tags($this->Email));
        $this->SoDienThoai = htmlspecialchars(strip_tags($this->SoDienThoai));

        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':HoTen',$this->HoTen);
        $stmt->bindParam(':GioiTinh',$this->GioiTinh);
        $stmt->bindParam(':NgaySinh',$this->NgaySinh);
        $stmt->bindParam(':Email',$this->Email);
        $stmt->bindParam(':SoDienThoai',$this->SoDienThoai);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
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