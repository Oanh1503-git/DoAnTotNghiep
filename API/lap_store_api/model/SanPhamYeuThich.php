<?php
// objects/SanPhamYeuThich.php
class SanPhamYeuThich {
    private $conn;
    private $table_name = "Sanphamyeuthich";

    public $id;
    public $MaSanPham;
    public $MaKhachHang;

    public function __construct($db) {
        $this->conn = $db;
    }

    public function check($MaSanPham, $MaKhachHang) {
        $query = "SELECT id FROM " . $this->table_name . " WHERE MaSanPham = ? AND MaKhachHang = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $MaSanPham);
        $stmt->bindParam(2, $MaKhachHang);
        $stmt->execute();
        return $stmt->rowCount() > 0;
    }

    public function create() {
        $query = "INSERT INTO " . $this->table_name . " 
                  SET MaSanPham=:MaSanPham, MaKhachHang=:MaKhachHang";
        
        $stmt = $this->conn->prepare($query);

        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));

        $stmt->bindParam(":MaSanPham", $this->MaSanPham);
        $stmt->bindParam(":MaKhachHang", $this->MaKhachHang);

        if ($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function delete() {
        $query = "DELETE FROM " . $this->table_name . " WHERE MaSanPham = :MaSanPham AND MaKhachHang = :MaKhachHang";
        $stmt = $this->conn->prepare($query);

        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));

        $stmt->bindParam(":MaSanPham", $this->MaSanPham);
        $stmt->bindParam(":MaKhachHang", $this->MaKhachHang);

        if ($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function readByKhachHang($MaKhachHang) {
        $query = "SELECT sp.id, sp.MaSanPham, sp.MaKhachHang, s.TenSanPham, s.Gia, h.DuongDan AS HinhAnh 
                  FROM Sanphamyeuthich sp 
                  JOIN sanpham s ON sp.MaSanPham = s.MaSanPham 
                  LEFT JOIN hinhanh h ON s.MaSanPham = h.MaSanPham AND h.MacDinh = 1 
                  WHERE sp.MaKhachHang = ? 
                  ORDER BY sp.id DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $MaKhachHang);
        $stmt->execute();
        return $stmt;
    }
}
?>