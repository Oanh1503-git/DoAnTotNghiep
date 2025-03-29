<?php
class ChiTietHoaDonBan {
    private $conn;

    // Thuộc tính của Chi Tiết Hóa Đơn Bán
    public $MaChiTietHoaDonBan;
    public $MaHoaDonBan;
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
            $query = "SELECT * FROM chitiethoadonban ORDER BY MaChiTietHoaDonBan ASC";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            return $stmt;
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return null;
        }
    }

    public function getChiTietHoaDonByMaHoaDonBan() {
        $query = "SELECT * 
                  FROM chitiethoadonban 
                  WHERE MaHoaDonBan = ?
                  ";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaHoaDonBan);
        $stmt->execute();
        return $stmt;
    }

    // Phương thức lấy chi tiết hóa đơn bán theo ID
    public function getDetailById() {
        try {
            $query = "SELECT * FROM chitiethoadonban WHERE MaChiTietHoaDonBan = ? LIMIT 1";
            $stmt = $this->conn->prepare($query);
            $stmt->bindParam(1, $this->MaChiTietHoaDonBan, PDO::PARAM_INT);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($row) {
                $this->MaHoaDonBan = $row['MaHoaDonBan'];
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
            $query = "INSERT INTO chitiethoadonban (MaHoaDonBan, MaSanPham, SoLuong, DonGia, ThanhTien, GiamGia) 
                        VALUES ((SELECT MAX(MaHoaDonBan) FROM HoaDonBan), :MaSanPham, :SoLuong, :DonGia, :ThanhTien, :GiamGia)";
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
            $query = "UPDATE chitiethoadonban SET 
                      MaHoaDonBan = :MaHoaDonBan, 
                      MaSanPham = :MaSanPham, 
                      SoLuong = :SoLuong, 
                      DonGia = :DonGia, 
                      ThanhTien = :ThanhTien, 
                      GiamGia = :GiamGia
                      WHERE MaChiTietHoaDonBan = :MaChiTietHoaDonBan";

            $stmt = $this->conn->prepare($query);

            // Làm sạch dữ liệu
            $this->MaHoaDonBan = htmlspecialchars(strip_tags($this->MaHoaDonBan));
            $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
            $this->SoLuong = htmlspecialchars(strip_tags($this->SoLuong));
            $this->DonGia = htmlspecialchars(strip_tags($this->DonGia));
            $this->ThanhTien = htmlspecialchars(strip_tags($this->ThanhTien));
            $this->GiamGia = htmlspecialchars(strip_tags($this->GiamGia));
            $this->MaChiTietHoaDonBan = htmlspecialchars(strip_tags($this->MaChiTietHoaDonBan));

            // Gắn tham số
            $stmt->bindParam(':MaHoaDonBan', $this->MaHoaDonBan);
            $stmt->bindParam(':MaSanPham', $this->MaSanPham);
            $stmt->bindParam(':SoLuong', $this->SoLuong);
            $stmt->bindParam(':DonGia', $this->DonGia);
            $stmt->bindParam(':ThanhTien', $this->ThanhTien);
            $stmt->bindParam(':GiamGia', $this->GiamGia);
            $stmt->bindParam(':MaChiTietHoaDonBan', $this->MaChiTietHoaDonBan);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }

    // Phương thức xóa chi tiết hóa đơn bán
    public function deleteDetail() {
        try {
            $query = "DELETE FROM chitiethoadonban WHERE MaChiTietHoaDonBan = :MaChiTietHoaDonBan";
            $stmt = $this->conn->prepare($query);

            $this->MaChiTietHoaDonBan = htmlspecialchars(strip_tags($this->MaChiTietHoaDonBan));

            $stmt->bindParam(':MaChiTietHoaDonBan', $this->MaChiTietHoaDonBan);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }
}
?>
