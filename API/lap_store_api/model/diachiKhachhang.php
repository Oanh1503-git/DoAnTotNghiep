<?php
class DiaChiKhachHang {
    private $conn;

    // Thuộc tính
    public $MaKhachHang;
    public $MaDiaChi;
    public $GhiChu;

    // Kết nối cơ sở dữ liệu
    public function __construct($database) {
        $this->conn = $database;
    }

    // Lấy tất cả địa chỉ của khách hàng
    public function GetAllDiaChiKhachHang() {
        $query = "SELECT * FROM diachikhachhang";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    // Lấy địa chỉ của một khách hàng cụ thể
    public function GetDiaChiByMaKhachHang() {
        $query = "SELECT *
                  FROM  diachikhachhang
                  WHERE MaKhachHang = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaKhachHang);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }

    // Thêm địa chỉ cho khách hàng
    public function AddDiaChiKhachHang() {
        $query = "INSERT INTO diachikhachhang SET MaKhachHang = :MaKhachHang, MaDiaChi = :MaDiaChi, GhiChu = :GhiChu";
        $stmt = $this->conn->prepare($query);

        // Xử lý dữ liệu
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));
        $this->GhiChu = htmlspecialchars(strip_tags($this->GhiChu));

        // Gắn tham số
        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':MaDiaChi', $this->MaDiaChi);
        $stmt->bindParam(':GhiChu', $this->GhiChu);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }

    // Cập nhật thông tin địa chỉ của khách hàng
    public function UpdateDiaChiKhachHang() {
        $query = "UPDATE diachikhachhang SET GhiChu = :GhiChu WHERE MaKhachHang = :MaKhachHang AND MaDiaChi = :MaDiaChi";
        $stmt = $this->conn->prepare($query);

        // Xử lý dữ liệu
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));
        $this->GhiChu = htmlspecialchars(strip_tags($this->GhiChu));

        // Gắn tham số
        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':MaDiaChi', $this->MaDiaChi);
        $stmt->bindParam(':GhiChu', $this->GhiChu);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }

    // Xóa địa chỉ của khách hàng
    public function DeleteDiaChiKhachHang() {
        $query = "DELETE FROM diachikhachhang WHERE MaKhachHang = :MaKhachHang AND MaDiaChi = :MaDiaChi";
        $stmt = $this->conn->prepare($query);

        // Xử lý dữ liệu
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaDiaChi = htmlspecialchars(strip_tags($this->MaDiaChi));

        // Gắn tham số
        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':MaDiaChi', $this->MaDiaChi);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }
}
?>
