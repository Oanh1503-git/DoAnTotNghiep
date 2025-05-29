<?php
class DiaChi{
    private $conn;

    //Thuoc tinh
    public $MaDiaChi;
    public $MaKhachHang;
    public $ThongTinDiaChi;
    public $TenNguoiNhan;
    public $SoDienThoai;
    public $MacDinh;
    //connect db

    public function __construct($database){
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllDiaChi() {
        $query = "SELECT * FROM diachi"; 
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }
    public function GetDiaChiById() {
        $query = "SELECT * FROM diachi WHERE MaDiaChi = ? LIMIT 1"; 
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1,$this->MaDiaChi);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        $this->MaDiaChi = $row['MaDiaChi'];
        $this->MaKhachHang = $row['MaKhachHang'];
        $this->ThongTinDiaChi = $row['ThongTinDiaChi'];
        $this->TenNguoiNhan = $row['TenNguoiNhan'];
        $this->SoDienThoai = $row['SoDienThoai'];
        $this->MacDinh = $row['MacDinh'];
    }  

    public function GetDiaChiByMaKhachHang() {
        $query = "SELECT *
                  FROM  diachi
                  WHERE MaKhachHang = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaKhachHang);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }

    public function GetDiaChiMacDinh() {
        $query = "SELECT * FROM diachi WHERE MaKhachHang = ? AND MacDinh = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaKhachHang);
        $stmt->bindParam(2, $this->MacDinh);
    
        $stmt->execute();
        return $stmt;
    }

    public function AddDiaChi() {
    $query = "INSERT INTO diachi SET  ThongTinDiaChi=:ThongTinDiaChi,MaKhachHang =:MaKhachHang, TenNguoiNhan =:TenNguoiNhan, SoDienThoai =:SoDienThoai, MacDinh =:MacDinh";

    $stmt = $this->conn->prepare($query);
    $this->ThongTinDiaChi = htmlspecialchars(strip_tags($this->ThongTinDiaChi));
    $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
    $this->TenNguoiNhan = htmlspecialchars(strip_tags($this->TenNguoiNhan));
    $this->SoDienThoai = htmlspecialchars(strip_tags($this->SoDienThoai));
    $this->MacDinh = htmlspecialchars(strip_tags($this->MacDinh));

    $stmt->bindParam(':ThongTinDiaChi', $this->ThongTinDiaChi);
    $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
    $stmt->bindParam(':TenNguoiNhan', $this->TenNguoiNhan);
    $stmt->bindParam(':SoDienThoai', $this->SoDienThoai);
    $stmt->bindParam(':MacDinh', $this->MacDinh);

    if ($stmt->execute()) {
        return true;
    }
    printf("Error: %s.\n", $stmt->error);
    return false;
}


    public function UpdateDiaChi(){
        $query = "UPDATE diachi SET ThongTinDiaChi=:ThongTinDiaChi, MaKhachHang =:MaKhachHang, TenNguoiNhan =:TenNguoiNhan, SoDienThoai =:SoDienThoai, MacDinh =:MacDinh WHERE MaDiaChi =:MaDiaChi";

        $stmt = $this->conn->prepare($query);


        $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));
        $this->ThongTinDiaChi = htmlspecialchars(strip_tags($this->ThongTinDiaChi));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->TenNguoiNhan = htmlspecialchars(strip_tags($this->TenNguoiNhan));
        $this->SoDienThoai = htmlspecialchars(strip_tags($this->SoDienThoai));
        $this->MacDinh = htmlspecialchars(strip_tags($this->MacDinh));


        $stmt->bindParam(':MaDiaChi', $this->MaDiaChi);
        $stmt->bindParam(':ThongTinDiaChi', $this->ThongTinDiaChi);
        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':TenNguoiNhan', $this->TenNguoiNhan);
        $stmt->bindParam(':SoDienThoai', $this->SoDienThoai);
        $stmt->bindParam(':MacDinh', $this->MacDinh);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function UpdateDiaChiMacDinh(){
        $query = "UPDATE diachi SET  MacDinh = 0 WHERE MaKhachHang =:MaKhachHang";

        $stmt = $this->conn->prepare($query);
        $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaKhachHang));
    

        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        
        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function deleteDiaChi(){
        $query = "DELETE FROM diachi WHERE MaDiaChi =:MaDiaChi";

        $stmt = $this->conn->prepare($query);

        $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));

        $stmt->bindParam(':MaDiaChi',$this->MaDiaChi);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }
}
?>