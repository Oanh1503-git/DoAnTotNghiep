<?php
class SanPham
{
    private $conn;

    //Thuoc tinh
    public $MaSanPham;
    public $TenSanPham;
    public $MaLoaiSanPham;
    public $mathuonghieu;
    public $CPU;
    public $RAM;
    public $CardManHinh;
    public $SSD;
    public $ManHinh;
    public $MaMauSac;
    public $Gia;
    public $SoLuong;
    public $MoTa;
    public $HinhAnh;
    public $TrangThai;

    public $MaKhachHang;

    public $SoLuongTrongGioHang;

    //connect db

    public function __construct($database)
    {
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllSanPham()
    {
        $query = "SELECT sp.*,ha.DuongDan FROM SanPham sp 
              join hinhanh ha on sp.MaSanPham = ha.MaSanPham where ha.MacDinh = 1";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }

    public function GetSanPhamById()
    {
    $query = "SELECT sp.*, ha.DuongDan 
              FROM SanPham sp 
              LEFT JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham AND ha.MacDinh = 1 
              WHERE sp.MaSanPham = ? 
              LIMIT 1";
    try {
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaSanPham, PDO::PARAM_INT);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($row) {
            // Gán giá trị từ kết quả vào các thuộc tính
            $this->TenSanPham = $row['TenSanPham'] ?? null;
            $this->MaLoaiSanPham = $row['MaLoaiSanPham'] ?? null;
            $this->mathuonghieu = $row['mathuonghieu'] ?? null;
            $this->CPU = $row['CPU'] ?? null;
            $this->RAM = $row['RAM'] ?? null;
            $this->CardManHinh = $row['CardManHinh'] ?? null;
            $this->SSD = $row['SSD'] ?? null;
            $this->ManHinh = $row['ManHinh'] ?? null;
            $this->MaMauSac = $row['MaMauSac'] ?? null;
            $this->Gia = $row['Gia'] ?? null;
            $this->SoLuong = $row['SoLuong'] ?? null;
            $this->MoTa = $row['MoTa'] ?? null;
            $this->HinhAnh = $row['DuongDan'] ?? null;
            $this->TrangThai = $row['TrangThai'] ?? null;
            return true;
        }
        return false;
    } catch (PDOException $e) {
        // Ghi log lỗi thay vì in trực tiếp
        error_log("Lỗi khi lấy sản phẩm: " . $e->getMessage());
        return false;
    }
    }

    public function GetSanPhamBySearch($searchTerm)
    {
        $query = "SELECT sp.* ,ha.DuongDan
          FROM SanPham sp
          JOIN HinhAnh ha ON sp.MaSanPham = ha.MaSanPham
          WHERE ha.MacDinh = 1 AND (
          sp.TenSanPham LIKE ? 
          OR sp.MoTa LIKE ? 
          OR sp.CPU LIKE ? 
          OR sp.RAM LIKE ? 
          OR sp.CardManHinh LIKE ? 
          OR sp.SSD LIKE ?
        )";
        $stmt = $this->conn->prepare($query);
        $searchTerm = "%" . $searchTerm . "%"; // Thêm dấu '%' để tìm kiếm theo kiểu "like"
        $stmt->bindParam(1, $searchTerm);
        $stmt->bindParam(2, $searchTerm);
        $stmt->bindParam(3, $searchTerm);
        $stmt->bindParam(4, $searchTerm);
        $stmt->bindParam(5, $searchTerm);
        $stmt->bindParam(6, $searchTerm);
        $stmt->execute();
        return $stmt;
    }
    public function SearchSanPham($search) {
        $query = "SELECT * FROM sanpham s LEFT JOIN hinhanh h ON s.MaSanPham = h.MaSanPham WHERE s.TenSanPham LIKE :search AND h.MacDinh = 1";
        $stmt = $this->conn->prepare($query);
        $search = "%$search%";
        $stmt->bindParam(':search', $search);
        $stmt->execute();
        return $stmt;
    }

    public function GetSanPhamByLoai()
    {
        $query = "SELECT sp.*,ha.DuongDan 
                  FROM SanPham sp 
                  join hinhanh ha on sp.MaSanPham = ha.MaSanPham
                  WHERE ha.MacDinh = 1 and sp.MaLoaiSanPham = ?
                  ";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaLoaiSanPham);
        $stmt->execute();
        return $stmt;
    }

    public function GetSanPhamTrongHoaDon($mahoadon)
    {
        $query = "SELECT sp.*, ha.DuongDan
                  FROM SanPham sp 
                  join hinhanh ha on sp.MaSanPham = ha.MaSanPham 
                  join ChiTietHoaDonBan cthd on sp.MaSanPham = cthd.MaSanPham
                  WHERE ha.MacDinh = 1 and cthd.MaHoaDonBan = ?
                  ";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $mahoadon);
        $stmt->execute();
        return $stmt;
    }

    public function GetSanPhamByGioHang()
    {
        $query = "SELECT sp.*, ha.DuongDan
                FROM sanpham sp 
                JOIN giohang gh on sp.MaSanPham = gh.MaSanPham
                JOIN hinhanh ha on sp.MaSanPham = ha.MaSanPham
                WHERE ha.MacDinh = 1 and gh.MaKhachHang = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaKhachHang);
        $stmt->execute();
        return $stmt;
    }

    public function AddSanPham()
    {
        $query = "INSERT INTO SanPham 
        (MaSanPham, TenSanPham, MaLoaiSanPham, CPU, RAM, CardManHinh, 
        SSD, ManHinh, MaMauSac, Gia, SoLuong, MoTa, TrangThai) 
        VALUES 
        (:MaSanPham, :TenSanPham, :MaLoaiSanPham, :CPU, :RAM, :CardManHinh, 
        :SSD, :ManHinh, :MaMauSac, :Gia, :SoLuong, :MoTa, :TrangThai)";

        $stmt = $this->conn->prepare($query);

        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));
        $this->TenSanPham = htmlspecialchars(strip_tags($this->TenSanPham));
        $this->MaLoaiSanPham = htmlspecialchars(strip_tags($this->MaLoaiSanPham));
        $this->CPU = htmlspecialchars(strip_tags($this->CPU));
        $this->RAM = htmlspecialchars(strip_tags($this->RAM));
        $this->CardManHinh = htmlspecialchars(strip_tags($this->CardManHinh));
        $this->SSD = htmlspecialchars(strip_tags($this->SSD));
        $this->ManHinh = htmlspecialchars(strip_tags($this->ManHinh));
        $this->MaMauSac = htmlspecialchars(strip_tags($this->MaMauSac));
        $this->Gia = htmlspecialchars(strip_tags($this->Gia));
        $this->SoLuong = htmlspecialchars(strip_tags($this->SoLuong));
        $this->MoTa = htmlspecialchars(strip_tags($this->MoTa));
        $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));


        $stmt->bindParam(':MaSanPham', $this->MaSanPham);
        $stmt->bindParam(':TenSanPham', $this->TenSanPham);
        $stmt->bindParam(':MaLoaiSanPham', $this->MaLoaiSanPham);
        $stmt->bindParam(':CPU', $this->CPU);
        $stmt->bindParam(':RAM', $this->RAM);
        $stmt->bindParam(':CardManHinh', $this->CardManHinh);
        $stmt->bindParam(':SSD', $this->SSD);
        $stmt->bindParam(':ManHinh', $this->ManHinh);
        $stmt->bindParam(':MaMauSac', $this->MaMauSac);
        $stmt->bindParam(':Gia', $this->Gia);
        $stmt->bindParam(':SoLuong', $this->SoLuong);
        $stmt->bindParam(':MoTa', $this->MoTa);
        $stmt->bindParam(':TrangThai', $this->TrangThai);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error %s.\n", $stmt->error);
        return false;
    }

    public function UpdateSanPham()
    {
        $query = "UPDATE SanPham 
        SET TenSanPham = :TenSanPham, 
        MaLoaiSanPham = :MaLoaiSanPham, 
        CPU = :CPU, 
        RAM = :RAM, 
        CardManHinh = :CardManHinh,
        SSD = :SSD, 
        ManHinh = :ManHinh, 
        MaMauSac = :MaMauSac, 
        Gia = :Gia, 
        SoLuong = :SoLuong, 
        MoTa = :MoTa, 
        TrangThai = :TrangThai 
        WHERE MaSanPham = :MaSanPham";


        $stmt = $this->conn->prepare($query);

        $this->TenSanPham = htmlspecialchars(strip_tags($this->TenSanPham));
        $this->MaLoaiSanPham = htmlspecialchars(strip_tags($this->MaLoaiSanPham));
        $this->CPU = htmlspecialchars(strip_tags($this->CPU));
        $this->RAM = htmlspecialchars(strip_tags($this->RAM));
        $this->CardManHinh = htmlspecialchars(strip_tags($this->CardManHinh));
        $this->SSD = htmlspecialchars(strip_tags($this->SSD));
        $this->ManHinh = htmlspecialchars(strip_tags($this->ManHinh));
        $this->MaMauSac = htmlspecialchars(strip_tags($this->MaMauSac));
        $this->Gia = htmlspecialchars(strip_tags($this->Gia));
        $this->SoLuong = htmlspecialchars(strip_tags($this->SoLuong));
        $this->MoTa = htmlspecialchars(strip_tags($this->MoTa));
        $this->TrangThai = htmlspecialchars(strip_tags($this->TrangThai));
        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));


        $stmt->bindParam(':TenSanPham', $this->TenSanPham);
        $stmt->bindParam(':MaLoaiSanPham', $this->MaLoaiSanPham);
        $stmt->bindParam(':CPU', $this->CPU);
        $stmt->bindParam(':RAM', $this->RAM);
        $stmt->bindParam(':CardManHinh', $this->CardManHinh);
        $stmt->bindParam(':SSD', $this->SSD);
        $stmt->bindParam(':ManHinh', $this->ManHinh);
        $stmt->bindParam(':MaMauSac', $this->MaMauSac);
        $stmt->bindParam(':Gia', $this->Gia);
        $stmt->bindParam(':SoLuong', $this->SoLuong);
        $stmt->bindParam(':MoTa', $this->MoTa);
        $stmt->bindParam(':TrangThai', $this->TrangThai);
        $stmt->bindParam(':MaSanPham', $this->MaSanPham);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error %s.\n", $stmt->error);
        return false;
    }

    public function DeleteSanPham()
    {
        $query = "DELETE FROM SanPham WHERE MaSanPham=:MaSanPham";

        $stmt = $this->conn->prepare($query);

        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));

        $stmt->bindParam(':MaSanPham', $this->MaSanPham);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error %s.\n", $stmt->error);
        return false;
    }
}
?>