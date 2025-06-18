<?php
class TaiKhoan{
    private $conn;

    //Thuoc tinh
    public $TenTaiKhoan;
    public $MaKhachHang;
    public $MatKhau;
    public $LoaiTaiKhoan;
    public $TrangThai;

    //connect db

    public function __construct($database){
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllTaiKhoan() {
        $query = "SELECT * FROM taikhoan"; 
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }

    public function GetTaiKhoanByUsername() {
        $query = "SELECT * FROM taikhoan WHERE TenTaiKhoan = ? LIMIT 1"; 
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1,$this->TenTaiKhoan);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        $this->TenTaiKhoan = $row['TenTaiKhoan'];
        $this->MaKhachHang = $row['MaKhachHang'];
        $this->MatKhau = $row['MatKhau'];
        $this->LoaiTaiKhoan = $row['LoaiTaiKhoan'];
        $this->TrangThai = $row['TrangThai'];
    }

    public function KiemTraDangNhap() {
        $query = "SELECT * FROM taikhoan WHERE TenTaiKhoan = ? AND MatKhau = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->TenTaiKhoan);
        $stmt->bindParam(2, $this->MatKhau);
    
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
    
        if ($row) {
            // Nếu tài khoản và mật khẩu đúng, trả về true
            return true;
        } else {
            // Nếu không tìm thấy tài khoản hoặc mật khẩu sai, trả về false
            return false;
        }
    }

    public function KiemTraTrungUsername() {
        $query = "SELECT * FROM taikhoan WHERE TenTaiKhoan = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->TenTaiKhoan);
   
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
    
        if ($row) {
            // Nếu tài khoản và mật khẩu đúng, trả về true
            return true;
        } else {
            // Nếu không tìm thấy tài khoản hoặc mật khẩu sai, trả về false
            return false;
        }
    }
    
public function AddTaiKhoan() {
    try {
        $query = "INSERT INTO taikhoan SET TenTaiKhoan=:TenTaiKhoan, MaKhachHang=:MaKhachHang, MatKhau=:MatKhau, LoaiTaiKhoan=:LoaiTaiKhoan, TrangThai=:TrangThai";
        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":TenTaiKhoan", $this->TenTaiKhoan);
        $stmt->bindParam(":MaKhachHang", $this->MaKhachHang);
        $stmt->bindParam(":MatKhau", $this->MatKhau);
        $stmt->bindParam(":LoaiTaiKhoan", $this->LoaiTaiKhoan);
        $stmt->bindParam(":TrangThai", $this->TrangThai);

        return $stmt->execute();
    } catch (PDOException $e) {
        return false;
    }
}


    public function UpdateTaiKhoan(){
        $query = "UPDATE taikhoan SET MatKhau =:MatKhau WHERE TenTaiKhoan=:TenTaiKhoan";

        $stmt = $this->conn->prepare($query);

        $this->TenTaiKhoan = htmlspecialchars(strip_tags($this->TenTaiKhoan));
        $this->MatKhau = htmlspecialchars(strip_tags($this->MatKhau));
        

        $stmt->bindParam(':TenTaiKhoan',$this->TenTaiKhoan);
        $stmt->bindParam(':MatKhau',$this->MatKhau);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function deleteTaiKhoan(){
        $query = "DELETE FROM taikhoan WHERE TenTaiKhoan=:TenTaiKhoan";

        $stmt = $this->conn->prepare($query);

        $this->MaKhachHang = htmlspecialchars(strip_tags($this->TenTaiKhoan));

        $stmt->bindParam(':TenTaiKhoan',$this->TenTaiKhoan);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }
}
?>