<?php
class giohang
{
    private $conn;

    //Thuoc tinh
    public $MaGioHang;
    public $MaKhachHang;
    public $MaSanPham;
    public $SoLuong;
    public $TrangThai;

    public $TongTien;
    //connect db

    public function __construct($database)
    {
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllGioHang()
    {
        $query = "SELECT * FROM giohang";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }
    public function GetGioHangById()
    {
        $query = "SELECT * FROM giohang WHERE MaGioHang = ? LIMIT 1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaGioHang);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        $this->MaGioHang = $row['MaGioHang'];
        $this->MaKhachHang = $row['MaKhachHang'];
        $this->MaSanPham = $row['MaSanPham'];
        $this->SoLuong = $row['SoLuong'];
        $this->TrangThai = $row['TrangThai'];
    }

    public function GetGioHangByMaKhachHang()
    {
        $query = "SELECT *
              FROM giohang
              WHERE MaKhachHang = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaKhachHang);
        $stmt->execute();
        return $stmt;
    }
    public function AddGioHang()
    {
        $query = "INSERT INTO giohang SET MaKhachHang=:MaKhachHang, MaSanPham=:MaSanPham, SoLuong=:SoLuong, TrangThai=:TrangThai";

        $stmt = $this->conn->prepare($query);

        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->SoLuong = htmlspecialchars(strip_tags($this->SoLuong));
        $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));

        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':MaSanPham', $this->MaSanPham);
        $stmt->bindParam(':SoLuong', $this->SoLuong);
        $stmt->bindParam(':TrangThai', $this->TrangThai);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }


    public function UpdateGioHang()
    {
        $query = "UPDATE giohang SET MaKhachHang=:MaKhachHang, MaSanPham=:MaSanPham, SoLuong=:SoLuong, TrangThai=:TrangThai WHERE MaGioHang=:MaGioHang";

        $stmt = $this->conn->prepare($query);

        $this->MaGioHang = htmlspecialchars(strip_tags($this->MaGioHang));
        $this->MaKhachHang = htmlspecialchars(strip_tags($this->MaKhachHang));
        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->SoLuong = filter_var($this->SoLuong, FILTER_VALIDATE_INT);
        $this->TrangThai = filter_var($this->TrangThai, FILTER_VALIDATE_INT);

        $stmt->bindParam(':MaGioHang', $this->MaGioHang);
        $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
        $stmt->bindParam(':MaSanPham', $this->MaSanPham);
        $stmt->bindParam(':SoLuong', $this->SoLuong);
        $stmt->bindParam(':TrangThai', $this->TrangThai);

        try {
            if ($stmt->execute()) {
                return true;
            } else {
                $error = $stmt->errorInfo();
                printf("Error: %s.\n", $error[2]);
                return false;
            }
        } catch (PDOException $e) {
            printf("Database error: %s.\n", $e->getMessage());
            return false;
        }
    }


    public function UpdateMultipleGioHang($giohangList)
    {
        foreach ($giohangList as $giohang) {
            // Giả sử giohang là một đối tượng chứa các thông tin như MaGioHang, MaKhachHang, MaSanPham, SoLuong
            $this->MaGioHang = $giohang['MaGioHang'];
            $this->MaKhachHang = $giohang['MaKhachHang'];
            $this->MaSanPham = $giohang['MaSanPham'];
            $this->SoLuong = $giohang['SoLuong'];
            $this->TrangThai = $giohang['TrangThai'];

            // Gọi hàm UpdateGioHang để cập nhật từng giỏ hàng
            $this->UpdateGioHang();
        }
        return true;
    }
    public function TinhTongTien()
    {
        $query = "SELECT SUM(sp.Gia * gh.SoLuong) AS TongTien
              FROM sanpham sp
              JOIN giohang gh ON sp.MaSanPham = gh.MaGioHang
              WHERE MaKhachHang = ?";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaKhachHang);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        // Gán kết quả vào thuộc tính
        $this->TongTien = isset($row['TongTien']) ? $row['TongTien'] : 0; // Mặc định là 0 nếu không có kết quả
    }


    public function deleteGioHang()
    {
        $query = "DELETE FROM giohang WHERE MaGioHang =:MaGioHang";

        $stmt = $this->conn->prepare($query);

        $this->MaGioHang = htmlspecialchars(strip_tags($this->MaGioHang));

        $stmt->bindParam(':MaGioHang', $this->MaGioHang);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error %s.\n", $stmt->error);
        return false;
    }
 public function deleteByKhachHangAndSanPham() {
    $query = "DELETE FROM giohang WHERE MaKhachHang = :MaKhachHang AND MaSanPham = :MaSanPham";
    $stmt = $this->conn->prepare($query);
    $stmt->bindParam(':MaKhachHang', $this->MaKhachHang);
    $stmt->bindParam(':MaSanPham', $this->MaSanPham);
    return $stmt->execute();
}


}
