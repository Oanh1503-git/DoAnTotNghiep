<?php
class DiaChi{
    private $conn;

    //Thuoc tinh
    public $MaDiaChi;
    public $MaKhachHang;
    public $ThongTinDiaChi;
    public $TenNguoiNhan;
    public$SoDienThoai;
    public$MacDinh;
    //connect db
    public $provinceId;
    public $districtId;
    public $wardId;
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
public function SetDiaChiMacDinh() {
    try {
        // Bắt đầu transaction
        $this->conn->beginTransaction();

        // Bước 1: Đặt tất cả địa chỉ của khách hàng này về không mặc định (MacDinh = 0)
        $query1 = "UPDATE diachi SET MacDinh = 0 WHERE MaKhachHang = :maKhachHang";
        $stmt1 = $this->conn->prepare($query1);
        $stmt1->bindParam(':maKhachHang', $this->MaKhachHang);
        $stmt1->execute();

        // Bước 2: Đặt địa chỉ được chọn là mặc định (MacDinh = 1)
        $query2 = "UPDATE diachi SET MacDinh = 1 WHERE MaDiaChi = :maDiaChi";
        $stmt2 = $this->conn->prepare($query2);
        $stmt2->bindParam(':maDiaChi', $this->MaDiaChi);
        $stmt2->execute();

        // Commit thay đổi
        $this->conn->commit();
        return true;

    } catch (PDOException $e) {
        // Có lỗi, rollback lại
        $this->conn->rollBack();
        error_log("Lỗi SetDiaChiMacDinh: " . $e->getMessage());
        return false;
    }
}

public function AddDiaChi() {
    $query = "INSERT INTO diachi 
        SET 
            ThongTinDiaChi = :ThongTinDiaChi,
            MaKhachHang = :MaKhachHang,
            TenNguoiNhan = :TenNguoiNhan,
            SoDienThoai = :SoDienThoai,
            MacDinh = :MacDinh,
            provinceId = :provinceId,
            districtId = :districtId,
            wardId = :wardId";

    $stmt = $this->conn->prepare($query);

    // Clean data
    $this->ThongTinDiaChi = htmlspecialchars(strip_tags($this->ThongTinDiaChi));
    $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
    $this->TenNguoiNhan = htmlspecialchars(strip_tags($this->TenNguoiNhan));
    $this->SoDienThoai = htmlspecialchars(strip_tags($this->SoDienThoai));
    $this->MacDinh = htmlspecialchars(strip_tags($this->MacDinh));
    $this->provinceId = htmlspecialchars(strip_tags($this->provinceId));
    $this->districtId = htmlspecialchars(strip_tags($this->districtId));
    $this->wardId = htmlspecialchars(strip_tags($this->wardId));

    // Bind params
    $stmt->bindParam(':ThongTinDiaChi', $this->ThongTinDiaChi);
    $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
    $stmt->bindParam(':TenNguoiNhan', $this->TenNguoiNhan);
    $stmt->bindParam(':SoDienThoai', $this->SoDienThoai);
    $stmt->bindParam(':MacDinh', $this->MacDinh);
    $stmt->bindParam(':provinceId', $this->provinceId);
    $stmt->bindParam(':districtId', $this->districtId);
    $stmt->bindParam(':wardId', $this->wardId);

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
    $query = "UPDATE diachi SET MacDinh = 0 WHERE MaKhachHang = :MaKhachHang";

    $stmt = $this->conn->prepare($query);

    // Làm sạch dữ liệu MaKhachHang
    $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));

    $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
    
    if($stmt->execute()){
        return true;
    }

    // In lỗi nếu có
    printf("Error %s.\n", $stmt->errorInfo()[2]);
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