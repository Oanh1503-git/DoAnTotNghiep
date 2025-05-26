<?php
class binhluandanhgia{
    private $conn;
    private $table_name = "binhluandanhgia";
    //Thuoc tinh
    public $MaBinhLuan;
    public $MaKhachHang;
    public $MaSanPham;
    public $MaHoaDonBan;
    public $SoSao;
    public $NoiDung;
    public $NgayDanhGia;
    public $TrangThai;
    //connect db
    
    public function __construct($database){
        $this->conn = $database;
    }

    //Doc dữ liệu
    public function create() {
        $query = "INSERT INTO " . $this->table_name . " 
                  SET MaKhachHang=:MaKhachHang, MaSanPham=:MaSanPham, MaHoaDonBan=:MaHoaDonBan, 
                      SoSao=:SoSao, NoiDung=:NoiDung, NgayDanhGia=:NgayDanhGia, TrangThai=:TrangThai";
        
        $stmt = $this->conn->prepare($query);

        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->MaHoaDonBan = htmlspecialchars(strip_tags($this->MaHoaDonBan));
        $this->SoSao = htmlspecialchars(strip_tags($this->SoSao));
        $this->NoiDung = htmlspecialchars(strip_tags($this->NoiDung));
        $this->NgayDanhGia = htmlspecialchars(strip_tags($this->NgayDanhGia));
        $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));

        $stmt->bindParam(":MaKhachHang", $this->MaKhachHang);
        $stmt->bindParam(":MaSanPham", $this->MaSanPham);
        $stmt->bindParam(":MaHoaDonBan", $this->MaHoaDonBan);
        $stmt->bindParam(":SoSao", $this->SoSao);
        $stmt->bindParam(":NoiDung", $this->NoiDung);
        $stmt->bindParam(":NgayDanhGia", $this->NgayDanhGia);
        $stmt->bindParam(":TrangThai", $this->TrangThai);

        if ($stmt->execute()) {
            return true;
        }
        return false;
    }

    

    public function GetAllBinhLuanDanhGia() {
        $query = "SELECT * FROM binhluandanhgia"; 
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }
    public function getBySanPham() {
    $query = "SELECT * FROM binhluandanhgia WHERE MaSanPham = ? ORDER BY NgayDanhGia DESC";
    $stmt = $this->conn->prepare($query);
    
    // Sanitize input
    $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
    $stmt->bindParam(1, $this->MaSanPham);

    try {
        $stmt->execute();
        return $stmt; // Trả về statement để xử lý danh sách bản ghi
    } catch (PDOException $e) {
        // Log lỗi nếu cần
        error_log("Error in getBySanPham: " . $e->getMessage());
        return false;
    }
}

    public function AddBinhLuanDanhGia(){
        $query = "INSERT INTO binhluandanhgia SET MaBinhLuan=:MaBinhLuan, MaKhachHang=:MaKhachHang, MaSanPham=:MaSanPham, MaHoaDonBan=:MaHoaDonBan, SoSao=:SoSao, NoiDung=:NoiDung, NgayDanhGia=:NgayDanhGia, TrangThai=:TrangThai";

        $stmt = $this->conn->prepare($query);

        $this->MaBinhLuan = htmlspecialchars(strip_tags($this->MaBinhLuan));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->MaHoaDonBan = htmlspecialchars(strip_tags($this->MaHoaDonBan));
        $this->SoSao = htmlspecialchars(strip_tags($this->SoSao));
        $this->NoiDung = htmlspecialchars(string: strip_tags($this->NoiDung));
        $this->NgayDanhGia = htmlspecialchars(string: strip_tags($this->NgayDanhGia));
        $this->TrangThai = htmlspecialchars(string: strip_tags($this->TrangThai));


        $stmt->bindParam(':MaBinhLuan',$this->MaBinhLuan);
        $stmt->bindParam(':MaKhachHang',$this->MaKhachHang);
        $stmt->bindParam(':MaSanPham',$this->MaSanPham);
        $stmt->bindParam(':MaHoaDonBan',$this->MaHoaDonBan);
        $stmt->bindParam(':SoSao',$this->SoSao);
        $stmt->bindParam(':NoiDung',$this->NoiDung);
        $stmt->bindParam(':NgayDanhGia',$this->NgayDanhGia);
        $stmt->bindParam(':TrangThai',$this->TrangThai);

        
        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function UpdateBinhLuanDanhGia() {
        $query = "UPDATE binhluandanhgia SET MaKhachHang=:MaKhachHang, MaSanPham=:MaSanPham, MaHoaDonBan=:MaHoaDonBan, SoSao=:SoSao, NoiDung=:NoiDung, NgayDanhGia=:NgayDanhGia, TrangThai=:TrangThai WHERE MaBinhLuan=:MaBinhLuan";

        $stmt = $this->conn->prepare($query);

        // Xử lý dữ liệu
        $this->MaBinhLuan = htmlspecialchars(strip_tags($this->MaBinhLuan));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->MaHoaDonBan = htmlspecialchars(strip_tags($this->MaHoaDonBan));
        $this->SoSao = htmlspecialchars(strip_tags($this->SoSao));
        $this->NoiDung = htmlspecialchars(strip_tags($this->NoiDung));
        $this->NgayDanhGia = htmlspecialchars(strip_tags($this->NgayDanhGia));
        $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));

        // Gán giá trị tham số
        $stmt->bindParam(':MaBinhLuan', $this->MaBinhLuan);
        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':MaSanPham', $this->MaSanPham);
        $stmt->bindParam(':MaHoaDonBan', $this->MaHoaDonBan);
        $stmt->bindParam(':SoSao', $this->SoSao);
        $stmt->bindParam(':NoiDung', $this->NoiDung);
        $stmt->bindParam(':NgayDanhGia', $this->NgayDanhGia);
        $stmt->bindParam(':TrangThai', $this->TrangThai);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }

    public function DeleteBinhLuanDanhGia() {
        $query = "DELETE FROM binhluandanhgia WHERE MaBinhLuan=:MaBinhLuan";

        $stmt = $this->conn->prepare($query);

        $this->MaBinhLuan = htmlspecialchars(strip_tags($this->MaBinhLuan));

        $stmt->bindParam(':MaBinhLuan', $this->MaBinhLuan);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }
}
?>