<?php
class loaisanpham{
    private $conn;

    //Thuoc tinh
    public $MaLoai;
    public $TenLoai;
    //connect db

    public function __construct($database){
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllLoaiSanPham() {
        $query = "SELECT * FROM loaisanpham"; 
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }
    public function GetLoaiSanPhamById() {
        $query = "SELECT * FROM loaisanpham WHERE MaLoai = ? LIMIT 1"; 
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1,$this->MaLoai);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        $this->MaLoai = $row['MaLoai'];
        $this->TenLoai = $row['TenLoai'];
    } 

    public function AddLoaiSanPham(){
        $query = "INSERT INTO loaisanpham SET TenLoai =:TenLoai ,  MaLoai =:MaLoai";

        $stmt = $this->conn->prepare($query);

        $this->MaLoai = htmlspecialchars(strip_tags($this->MaLoai));
        $this->TenLoai = htmlspecialchars(strip_tags($this->TenLoai));


        $stmt->bindParam(':MaLoai',$this->MaLoai);
        $stmt->bindParam(':TenLoai',$this->TenLoai);
        
        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function UpdateLoaiSanPham(){
        $query = "UPDATE loaisanpham SET TenLoai =:TenLoai  WHERE MaLoai=:MaLoai";

        $stmt = $this->conn->prepare($query);

        $this->MaLoai = htmlspecialchars(strip_tags($this->MaLoai));
        $this->TenLoai = htmlspecialchars(strip_tags($this->TenLoai));

        $stmt->bindParam(':MaLoai',$this->MaLoai);
        $stmt->bindParam(':TenLoai',$this->TenLoai);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function deleteLoaiSanPham(){
        $query = "DELETE FROM loaisanpham WHERE MaLoai=:MaLoai";

        $stmt = $this->conn->prepare($query);

        $this->MaLoai = htmlspecialchars(strip_tags($this->MaLoai));

        $stmt->bindParam(':MaLoai',$this->MaLoai);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }
}
?>