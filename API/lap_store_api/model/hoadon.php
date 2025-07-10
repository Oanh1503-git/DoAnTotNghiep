<?php
class HoaDon
{
    private $conn;

    // Thuộc tính liên quan đến hóa đơn bán
    public $MaHoaDon;
    public $MaKhachHang;
    public $NgayDatHang;
    public $MaDiaChi;
    public $TongTien;
    public $PhuongThucThanhToan;
    public $TrangThai;

    // Kết nối cơ sở dữ liệu
    public function __construct($database)
    {
        $this->conn = $database;
    }

    // Phương thức lấy tất cả hóa đơn bán
    public function getAllHoaDon()
    {
        try {
            $query = "SELECT * FROM hoadon ORDER BY MaHoaDon DESC";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            return $stmt;
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return null;
        }
    }

    public function getAllHoaDonByKhachHang()
    {
        try {
            $query = "SELECT * FROM hoadon WHERE MaKhachHang = ? AND TrangThai = ? ORDER BY MaHoaDon DESC";
            $stmt = $this->conn->prepare($query);

            // Gắn giá trị cho từng tham số với đúng vị trí
            $stmt->bindParam(1, $this->MaKhachHang, PDO::PARAM_INT);
            $stmt->bindParam(2, $this->TrangThai, PDO::PARAM_INT); // Đổi vị trí thành 2

            $stmt->execute();
            return $stmt;
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return null;
        }
    }

    public function getAllHoaDonTheoTrangThai()
    {
        try {
            $query = "SELECT * FROM hoadon WHERE TrangThai = ? ORDER BY NgayDatHang DESC";
            $stmt = $this->conn->prepare($query);

            // Gắn giá trị cho từng tham số với đúng vị trí
            $stmt->bindParam(1, $this->TrangThai, PDO::PARAM_INT); // Đổi vị trí thành 2

            $stmt->execute();
            return $stmt;
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return null;
        }
    }


    // Phương thức lấy hóa đơn bán theo ID
    public function getHoaDonById()
    {
        try {
            $query = "SELECT * FROM hoadon WHERE MaHoaDon = ? LIMIT 1";
            $stmt = $this->conn->prepare($query);
            $stmt->bindParam(1, $this->MaHoaDon, PDO::PARAM_INT);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($row) {

                $this->MaKhachHang = $row['MaKhachHang'];
                $this->NgayDatHang = $row['NgayDatHang'];
                $this->MaDiaChi = $row['MaDiaChi'];
                $this->TongTien = $row['TongTien'];
                $this->PhuongThucThanhToan = $row['PhuongThucThanhToan'];
                $this->TrangThai = $row['TrangThai'];
            }
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
        }
    }


    // Phương thức thêm hóa đơn bán
    public function addHoaDon()
    {
        try {
            $query = "INSERT INTO hoadon (MaHoaDon, MaKhachHang, NgayDatHang, MaDiaChi, TongTien, PhuongThucThanhToan, TrangThai) 
                      VALUES (:MaHoaDon, :MaKhachHang, :NgayDatHang, :MaDiaChi, :TongTien, :PhuongThucThanhToan, :TrangThai)";
            $stmt = $this->conn->prepare($query);

            // Làm sạch dữ liệu đầu vào
            $this->MaHoaDon = htmlspecialchars(strip_tags($this->MaHoaDon));
            $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
            $this->NgayDatHang = htmlspecialchars(strip_tags($this->NgayDatHang));
            $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));
            $this->TongTien = htmlspecialchars(strip_tags($this->TongTien));
            $this->PhuongThucThanhToan = htmlspecialchars(strip_tags($this->PhuongThucThanhToan));
            $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));

            // Gắn tham số
            $stmt->bindParam(':MaHoaDon', $this->MaHoaDon);
            $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
            $stmt->bindParam(':NgayDatHang', $this->NgayDatHang);
            $stmt->bindParam(':MaDiaChi', $this->MaDiaChi);
            $stmt->bindParam(':TongTien', $this->TongTien);
            $stmt->bindParam(':PhuongThucThanhToan', $this->PhuongThucThanhToan);
            $stmt->bindParam(':TrangThai', $this->TrangThai);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }

    // Phương thức cập nhật hóa đơn bán
    public function updateHoaDon()
    {
        try {
            $query = "UPDATE hoadon SET 
                      MaKhachHang = :MaKhachHang, 
                      NgayDatHang = :NgayDatHang, 
                      MaDiaChi = :MaDiaChi, 
                      TongTien = :TongTien,
                      PhuongThucThanhToan = :PhuongThucThanhToan, 
                      TrangThai = :TrangThai 
                      WHERE MaHoaDon = :MaHoaDon";

            $stmt = $this->conn->prepare($query);

            // Làm sạch dữ liệu
            $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
            $this->NgayDatHang = htmlspecialchars(strip_tags($this->NgayDatHang));
            $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));
            $this->TongTien = htmlspecialchars(strip_tags($this->TongTien));
            $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));
            $this->PhuongThucThanhToan = htmlspecialchars(strip_tags($this->PhuongThucThanhToan));
            $this->MaHoaDon = htmlspecialchars(strip_tags($this->MaHoaDon));

            // Gắn tham số
            $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
            $stmt->bindParam(':NgayDatHang', $this->NgayDatHang);
            $stmt->bindParam(':MaDiaChi', $this->MaDiaChi);
            $stmt->bindParam(':TongTien', $this->TongTien);
            $stmt->bindParam(':PhuongThucThanhToan', $this->PhuongThucThanhToan);
            $stmt->bindParam(':TrangThai', $this->TrangThai);
            $stmt->bindParam(':MaHoaDon', $this->MaHoaDon);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }

    // Phương thức lấy MaHoaDon lớn nhất
    public function getMaxMaHoaDon() {
        try {
            $query = "SELECT MAX(MaHoaDon) AS MaxMaHoaDon FROM hoadon";
            $stmt = $this->conn->prepare($query);
            $stmt->execute();
            
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            // Nếu có kết quả trả về
            if ($row) {
                return $row['MaxMaHoaDon'];
            }
            
            return null;
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return null;
        }
    }
    public function updateTrangThai() {
    $query = "UPDATE hoadon SET TrangThai = :TrangThai WHERE MaHoaDon = :MaHoaDon";
    $stmt = $this->conn->prepare($query);

    // Sanitize data
    $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));
    $this->MaHoaDon = htmlspecialchars(strip_tags($this->MaHoaDon));

    // Bind data
    $stmt->bindParam(":TrangThai", $this->TrangThai);
    $stmt->bindParam(":MaHoaDon", $this->MaHoaDon);

    if ($stmt->execute()) {
        return $stmt->rowCount() > 0; // Trả về true nếu có dòng được cập nhật
    } else {
        // Ghi log lỗi chi tiết
        $errorInfo = $stmt->errorInfo();
        error_log("updateTrangThai SQL error: " . implode(" | ", $errorInfo));
        return false;
    }
}



public function getDonHangDayDuTheoKhachHang($MaKhachHang)
{
    try {
        $query = "
            SELECT 
                hd.MaHoaDon, hd.NgayDatHang, hd.TongTien, hd.TrangThai, hd.PhuongThucThanhToan,
                sp.TenSanPham, sp.Gia, cthd.SoLuong, cthd.ThanhTien, cthd.MaSanPham,
                ha.DuongDan AS HinhAnh
            FROM hoadon hd
            JOIN chitiethoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon
            JOIN sanpham sp ON cthd.MaSanPham = sp.MaSanPham
            LEFT JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham AND ha.MacDinh = 1
            WHERE hd.MaKhachHang = :MaKhachHang
            ORDER BY hd.MaHoaDon DESC
        ";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(':MaKhachHang', $MaKhachHang);
        $stmt->execute();

        return $stmt;

    } catch (PDOException $e) {
        error_log("Lỗi khi lấy đơn hàng đầy đủ: " . $e->getMessage());
        return null;
    }
}

    


    // Phương thức xóa hóa đơn bán
    public function deleteHoaDon()
    {
        try {
            $query = "DELETE FROM hoadon WHERE MaHoaDon = :MaHoaDon";
            $stmt = $this->conn->prepare($query);

            $this->MaHoaDon = htmlspecialchars(strip_tags($this->MaHoaDon));

            $stmt->bindParam(':MaHoaDon', $this->MaHoaDon);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Lỗi: " . $e->getMessage();
            return false;
        }
    }
}
?>
