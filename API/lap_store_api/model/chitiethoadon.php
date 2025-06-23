<?php
class ChiTietHoaDon {
    private $conn;

    // Thuộc tính của Chi Tiết Hóa Đơn Bán
    public $MaChiTietHoaDon;
    public $MaHoaDon;
    public $MaSanPham;
    public $SoLuong;
    public $DonGia;
    public $ThanhTien;
    public $GiamGia;

    // Kết nối cơ sở dữ liệu
    public function __construct($database) {
        $this->conn = $database;
    }

    // Phương thức lấy tất cả chi tiết hóa đơn bán
    public function getAllChiTietHoaDon() {
        try {
            $query = "SELECT * FROM chitiethoadon ORDER BY MaChiTietHoaDon ASC";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            return $stmt;
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return null;
        }
    }

    public function getChiTietHoaDonByMaHoaDon() {
        $query = "SELECT * 
                  FROM chitiethoadon 
                  WHERE MaHoaDon = ?
                  ";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaHoaDon);
        $stmt->execute();
        return $stmt;
    }

    // Phương thức lấy chi tiết hóa đơn bán theo ID
    public function getDetailById() {
        try {
            $query = "SELECT * FROM chitiethoadon WHERE MaChiTietHoaDon = ? LIMIT 1";
            $stmt = $this->conn->prepare($query);
            $stmt->bindParam(1, $this->MaChiTietHoaDon, PDO::PARAM_INT);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($row) {
                $this->MaHoaDon = $row['MaHoaDon'];
                $this->MaSanPham = $row['MaSanPham'];
                $this->SoLuong = $row['SoLuong'];
                $this->DonGia = $row['DonGia'];
                $this->ThanhTien = $row['ThanhTien'];
                $this->GiamGia = $row['GiamGia'];
            }
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
        }
    }

    // Phương thức thêm chi tiết hóa đơn bán
    public function addDetail() {
        try {
              $query = "INSERT INTO chitiethoadon (MaHoaDon, MaSanPham, SoLuong, DonGia, GiamGia)
            VALUES (:MaHoaDon, :MaSanPham, :SoLuong, :DonGia, :GiamGia)";

            $stmt = $this->conn->prepare($query);

            // Làm sạch dữ liệu đầu vào
            $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
            $this->SoLuong = htmlspecialchars(strip_tags($this->SoLuong));
            $this->DonGia = htmlspecialchars(strip_tags($this->DonGia));
            $this->ThanhTien = htmlspecialchars(strip_tags($this->ThanhTien));
            $this->GiamGia = htmlspecialchars(strip_tags($this->GiamGia));

            // Gắn tham số
            $stmt->bindParam(':MaSanPham', $this->MaSanPham);
            $stmt->bindParam(':SoLuong', $this->SoLuong);
            $stmt->bindParam(':DonGia', $this->DonGia);
            $stmt->bindParam(':ThanhTien', $this->ThanhTien);
            $stmt->bindParam(':GiamGia', $this->GiamGia);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }

    // Phương thức cập nhật chi tiết hóa đơn bán
    public function updateDetail() {
        try {
           $query = "UPDATE chitiethoadon SET 
          MaHoaDon = :MaHoaDon, 
          MaSanPham = :MaSanPham, 
          SoLuong = :SoLuong, 
          DonGia = :DonGia, 
          GiamGia = :GiamGia
          WHERE MaChiTietHoaDon = :MaChiTietHoaDon";

            $stmt = $this->conn->prepare($query);

            // Làm sạch dữ liệu
            $this->MaHoaDon = htmlspecialchars(strip_tags($this->MaHoaDon));
            $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
            $this->SoLuong = htmlspecialchars(strip_tags($this->SoLuong));
            $this->DonGia = htmlspecialchars(strip_tags($this->DonGia));
            $this->ThanhTien = htmlspecialchars(strip_tags($this->ThanhTien));
            $this->GiamGia = htmlspecialchars(strip_tags($this->GiamGia));
            $this->MaChiTietHoaDon = htmlspecialchars(strip_tags($this->MaChiTietHoaDon));

            // Gắn tham số
            $stmt->bindParam(':MaHoaDon', $this->MaHoaDon);
            $stmt->bindParam(':MaSanPham', $this->MaSanPham);
            $stmt->bindParam(':SoLuong', $this->SoLuong);
            $stmt->bindParam(':DonGia', $this->DonGia);
            $stmt->bindParam(':ThanhTien', $this->ThanhTien);
            $stmt->bindParam(':GiamGia', $this->GiamGia);
            $stmt->bindParam(':MaChiTietHoaDon', $this->MaChiTietHoaDon);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }

    // Phương thức xóa chi tiết hóa đơn bán
    public function deleteDetail() {
        try {
            $query = "DELETE FROM chitiethoadon WHERE MaChiTietHoaDon = :MaChiTietHoaDon";
            $stmt = $this->conn->prepare($query);

            $this->MaChiTietHoaDon = htmlspecialchars(strip_tags($this->MaChiTietHoaDon));

            $stmt->bindParam(':MaChiTietHoaDon', $this->MaChiTietHoaDon);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }
}
?>
