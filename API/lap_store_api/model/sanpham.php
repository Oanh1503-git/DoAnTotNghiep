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
    public $GiamGia;
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
        $query = "SELECT sp.*,ha.DuongDan FROM sanpham sp 
              join hinhanh ha on sp.MaSanPham = ha.MaSanPham where ha.MacDinh = 1";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }

    public function GetSanPhamById()
    {
        $query = "SELECT sp.*, ha.DuongDan FROM sanpham sp 
              JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham
              WHERE ha.MacDinh = 1 and sp.MaSanPham = ? LIMIT 1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->MaSanPham);
        $stmt->execute();

        // Lấy kết quả
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($row) {
            // Gán giá trị từ kết quả vào các thuộc tính của đối tượng
            $this->TenSanPham = $row['TenSanPham'] ?? null;
            $this->MaLoaiSanPham = $row['MaLoaiSanPham'] ?? null;
            $this->mathuonghieu = $row['mathuonghieu'] ?? 1;
            $this->CPU = $row['CPU'] ?? null;
            $this->RAM = $row['RAM'] ?? null;
            $this->CardManHinh = $row['CardManHinh'] ?? null;
            $this->SSD = $row['SSD'] ?? null;
            $this->ManHinh = $row['ManHinh'] ?? null;
            $this->MaMauSac = $row['MaMauSac'] ?? null;
            $this->Gia = $row['Gia'] ?? null;
            $this->GiamGia = $row['GiamGia'] ?? 0;
            $this->SoLuong = $row['SoLuong'] ?? null;
            $this->MoTa = $row['MoTa'] ?? null;
            $this->HinhAnh = $row['DuongDan'] ?? null;
            $this->TrangThai = $row['TrangThai'] ?? null;
        } else {
            // Không tìm thấy sản phẩm, có thể thông báo lỗi
            echo "Sản phẩm không tồn tại.";
            return false;
        }

        // Giải phóng bộ nhớ
        unset($row);
    }
public function getSanPhamByPriceRange($minPrice, $maxPrice)
{
    $query = "SELECT sp.*, ha.DuongDan
              FROM sanpham sp
              JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham
              WHERE ha.MacDinh = 1
              AND sp.Gia BETWEEN :min AND :max
              AND sp.TrangThai = 1";

    $stmt = $this->conn->prepare($query);
    $stmt->bindParam(':min', $minPrice, PDO::PARAM_INT);
    $stmt->bindParam(':max', $maxPrice, PDO::PARAM_INT);
    $stmt->execute();

    return $stmt;
}

    public function GetSanPhamBySearch($searchTerm)
    {
        $query = "SELECT sp.* ,ha.DuongDan
          FROM sanpham sp
          JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham
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
    public function GetSanPhamByBrand($brand)
    {
    $query = "SELECT sp.*, ha.DuongDan
              FROM sanpham sp
              JOIN hinhanh ha ON sp.MaSanPham = ha.MaSanPham
              WHERE ha.MacDinh = 1 AND sp.ThuongHieu LIKE ?";
              
    $stmt = $this->conn->prepare($query);
    $brand = "%" . $brand . "%";
    $stmt->bindParam(1, $brand);
    $stmt->execute();
    return $stmt;
    }


    public function GetSanPhamByLoai()
    {
        $query = "SELECT sp.*,ha.DuongDan 
                  FROM sanpham sp 
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
                  FROM sanpham sp 
                  join hinhanh ha on sp.MaSanPham = ha.MaSanPham 
                  join ChiTietHoaDon cthd on sp.MaSanPham = cthd.MaSanPham
                  WHERE ha.MacDinh = 1 and cthd.MaHoaDon = ?
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
        $query = "INSERT INTO sanpham 
        (MaSanPham, TenSanPham, MaLoaiSanPham, CPU, RAM, CardManHinh, 
        SSD, ManHinh, MaMauSac, Gia, SoLuong, MoTa, HinhAnh, TrangThai) 
        VALUES 
        (:MaSanPham, :TenSanPham, :MaLoaiSanPham, :CPU, :RAM, :CardManHinh, 
        :SSD, :ManHinh, :MaMauSac, :Gia, :SoLuong, :MoTa, :HinhAnh, :TrangThai)";

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
        $this->HinhAnh = htmlspecialchars(strip_tags($this->HinhAnh));
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
        $stmt->bindParam(':HinhAnh', $this->HinhAnh);
        $stmt->bindParam(':TrangThai', $this->TrangThai);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error %s.\n", $stmt->error);
        return false;
    }

    public function UpdateSanPham()
    {
        $query = "UPDATE sanpham 
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
        HinhAnh = :HinhAnh, 
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
        $this->HinhAnh = htmlspecialchars(strip_tags($this->HinhAnh));
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
        $stmt->bindParam(':HinhAnh', $this->HinhAnh);
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
        $query = "DELETE FROM sanpham WHERE MaSanPham=:MaSanPham";

        $stmt = $this->conn->prepare($query);

        $this->MaSanPham = htmlspecialchars(strip_tags($this->MaSanPham));

        $stmt->bindParam(':MaSanPham', $this->MaSanPham);

        if ($stmt->execute()) {
            return true;
        }
        printf("Error %s.\n", $stmt->error);
        return false;
    }
     public function KiemTraSoLuong() {
        // Bước 1: Tổng số lượng trong giỏ
        $sqlGioHang = "SELECT SUM(SoLuong) AS TongSoLuong FROM giohang WHERE MaKhachHang = ? AND MaSanPham = ?";
        $stmtGioHang = $this->conn->prepare($sqlGioHang);
        $stmtGioHang->execute([$this->MaKhachHang, $this->MaSanPham]);
        $gioHangData = $stmtGioHang->fetch(PDO::FETCH_ASSOC);
        $soLuongTrongGio = $gioHangData['TongSoLuong'] ?? 0;

        // Bước 2: Số lượng trong kho
        $sqlSanPham = "SELECT SoLuong FROM sanpham WHERE MaSanPham = ?";
        $stmtSanPham = $this->conn->prepare($sqlSanPham);
        $stmtSanPham->execute([$this->MaSanPham]);
        $sanPhamData = $stmtSanPham->fetch(PDO::FETCH_ASSOC);

        if (!$sanPhamData) {
            return [
                'status' => 'error',
                'message' => 'Không tìm thấy sản phẩm'
            ];
        }

        $soLuongTrongKho = $sanPhamData['SoLuong'];

        // Bước 3: So sánh
        if ($soLuongTrongGio >= $soLuongTrongKho) {
            return [
                'status' => 'fail',
                'message' => 'Số lượng sản phẩm trong giỏ hàng đã vượt quá tồn kho',
                'soLuongGioHang' => (int)$soLuongTrongGio,
                'soLuongKho' => (int)$soLuongTrongKho
            ];
        } else {
            return [
                'status' => 'ok',
                'message' => 'Số lượng còn hợp lệ',
                'soLuongGioHang' => (int)$soLuongTrongGio,
                'soLuongKho' => (int)$soLuongTrongKho
            ];
        }
    }
public function getSoLuongKho($MaSanPham) {
    try {
        $query = "SELECT SoLuong FROM sanpham WHERE MaSanPham = :MaSanPham LIMIT 1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(':MaSanPham', $MaSanPham);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($row) {
            return $row['SoLuong'];
        } else {
            return null;
        }
    } catch (PDOException $e) {
        error_log("Lỗi khi lấy số lượng tồn kho: " . $e->getMessage());
        return null;
    }
}

    public function truSoLuongTrongKho($soLuongCanTru) {
    // Bước 1: Lấy số lượng hiện tại từ CSDL
    $query = "SELECT SoLuong FROM sanpham WHERE MaSanPham = ?";
    $stmt = $this->conn->prepare($query);
    $stmt->bindParam(1, $this->MaSanPham);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $soLuongHienTai = (int)$row['SoLuong'];

        // Bước 2: Kiểm tra nếu số lượng cần trừ nhiều hơn số lượng trong kho
        if ($soLuongCanTru > $soLuongHienTai) {
            return [
                'success' => false,
                'message' => 'Số lượng trong kho không đủ'
            ];
        }

        // Bước 3: Cập nhật lại số lượng
        $soLuongMoi = $soLuongHienTai - $soLuongCanTru;
        $updateQuery = "UPDATE sanpham SET SoLuong = :SoLuongMoi WHERE MaSanPham = :MaSanPham";
        $updateStmt = $this->conn->prepare($updateQuery);
        $updateStmt->bindParam(':SoLuongMoi', $soLuongMoi);
        $updateStmt->bindParam(':MaSanPham', $this->MaSanPham);

        if ($updateStmt->execute()) {
            return [
                'success' => true,
                'message' => 'Trừ số lượng thành công',
                'SoLuongConLai' => $soLuongMoi
            ];
        } else {
            return [
                'success' => false,
                'message' => 'Không thể cập nhật số lượng'
            ];
        }
    } else {
        return [
            'success' => false,
            'message' => 'Không tìm thấy sản phẩm'
        ];
    }
}



}
?>