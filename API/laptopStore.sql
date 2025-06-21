-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 16, 2025 lúc 08:15 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `laptopsh_laptopstore`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKhachHang` varchar(50) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `GioiTinh` char(3) DEFAULT NULL,
  `NgaySinh` date DEFAULT NULL,
  `Email` varchar(100) NOT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm index cho MaKhachHang
ALTER TABLE `khachhang` ADD INDEX `idx_makhachhang` (`MaKhachHang`);

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `MaLoai` int(11) NOT NULL AUTO_INCREMENT,
  `TenLoai` varchar(50) NOT NULL,
  PRIMARY KEY (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `Thuonghieu`
--

CREATE TABLE `Thuonghieu` (
  `mathuonghieu` int(11) NOT NULL ,
  `tenthuonghieu` varchar(50) NOT NULL,
  PRIMARY KEY (`mathuonghieu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `mausac`
--

CREATE TABLE `mausac` (
  `MaMauSac` int(11) NOT NULL AUTO_INCREMENT,
  `TenMauSac` varchar(20) DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  PRIMARY KEY (`MaMauSac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `MaSanPham` int(11) NOT NULL AUTO_INCREMENT,
  `TenSanPham` varchar(100) NOT NULL,
  `MaLoaiSanPham` int(11) DEFAULT NULL,
  `mathuonghieu` int(11) NOT NULL,
  `CPU` varchar(50) DEFAULT NULL,
  `RAM` varchar(10) DEFAULT NULL,
  `CardManHinh` varchar(20) DEFAULT NULL,
  `SSD` varchar(10) DEFAULT NULL,
  `ManHinh` varchar(50) DEFAULT NULL,
  `MaMauSac` int(11) DEFAULT NULL,
  `Gia` int(11) DEFAULT NULL,
  `SoLuong` int(11) DEFAULT NULL,
  `MoTa` text DEFAULT NULL,
  `TrangThai` int(11) DEFAULT NULL,
  PRIMARY KEY (`MaSanPham`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `diachi`
--

CREATE TABLE `diachi` (
  `MaDiaChi` int(11) NOT NULL AUTO_INCREMENT,
  `ThongTinDiaChi` varchar(200) NOT NULL,
  `TenNguoiNhan` varchar(100) NOT NULL,
  `SoDienThoai` char(10) NOT NULL,
  `MaKhachHang` varchar(50) NOT NULL,
  `MacDinh` int(1) NOT NULL,
  PRIMARY KEY (`MaDiaChi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `MaHoaDon` int(11) NOT NULL AUTO_INCREMENT,
  `MaKhachHang` varchar(50) NOT NULL,
  `NgayDatHang` date NOT NULL,
  `MaDiaChi` int(11) DEFAULT NULL,
  `TongTien` int(11) DEFAULT NULL,
  `PhuongThucThanhToan` varchar(50) NOT NULL,
  `TrangThai` varchar(20) NOT NULL,
  PRIMARY KEY (`MaHoaDon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `MaChiTietHoaDon` int(11) NOT NULL AUTO_INCREMENT,
  `MaHoaDon` int(11) DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL,
  `DonGia` int(20) DEFAULT NULL,
  `ThanhTien` int(20) GENERATED ALWAYS AS (`SoLuong` * `DonGia`) STORED,
  `GiamGia` int(20) DEFAULT NULL,
  PRIMARY KEY (`MaChiTietHoaDon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `binhluandanhgia`
--

CREATE TABLE `binhluandanhgia` (
  `MaBinhLuan` int(11) NOT NULL AUTO_INCREMENT,
  `MaKhachHang` varchar(50) NOT NULL,
  `MaSanPham` int(11) NOT NULL,
  `MaHoaDon` int(11) NOT NULL,
  `SoSao` int(11) NOT NULL,
  `NoiDung` text DEFAULT NULL,
  `NgayDanhGia` datetime NOT NULL,
  `TrangThai` int(11) DEFAULT NULL,
  PRIMARY KEY (`MaBinhLuan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `giohang`
--

CREATE TABLE `giohang` (
  `MaGioHang` int(11) NOT NULL AUTO_INCREMENT,
  `MaKhachHang` varchar(50) NOT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL,
  `TrangThai` int(11) DEFAULT NULL,
  PRIMARY KEY (`MaGioHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `hinhanh`
--

CREATE TABLE `hinhanh` (
  `MaHinhAnh` int(11) NOT NULL AUTO_INCREMENT,
  `DuongDan` varchar(500) DEFAULT NULL,
  `MacDinh` int(1) NOT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  PRIMARY KEY (`MaHinhAnh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `Sanphamyeuthich`
--

CREATE TABLE `Sanphamyeuthich` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `MaSanPham` int(11) NOT NULL,
  `MaKhachHang` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `TenTaiKhoan` varchar(50) NOT NULL,            -- Khóa chính (nên đặt)
  `MaKhachHang` varchar(50) NOT NULL,            -- Liên kết với bảng khách hàng
  `MatKhau` varchar(255) NOT NULL,               -- Mã hóa mật khẩu (bằng bcrypt/hash)
  `LoaiTaiKhoan` int(1) NOT NULL,                -- 0: khách, 1: admin (tuỳ hệ thống)
  `TrangThai` int(1) DEFAULT NULL              -- Trạng thái hoạt động (0: khóa, 1: hoạt động)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


--
-- Đang đổ dữ liệu cho bảng `khachhang`
--
CREATE TABLE `otp` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `TenTaiKhoan` VARCHAR(50) NOT NULL,
  `otp_code` VARCHAR(20) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` DATETIME NOT NULL,
  `is_used` TINYINT(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cấu trúc bảng cho tin nhắn
CREATE TABLE `tin_nhan` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `MaKhachHang` VARCHAR(50) NOT NULL,
  `NguoiGui` ENUM('admin', 'khach') NOT NULL,
  `NoiDung` TEXT NOT NULL,
  `ThoiGianGui` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `LoaiTinNhan` ENUM('bao_hanh', 'khieu_nai', 'khac') DEFAULT 'khac'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Cấu trúc bảng cho giao dịch MoMo
CREATE TABLE `giaodich_momo` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `MaHoaDon` INT NOT NULL,
  `orderId` VARCHAR(100) NOT NULL,
  `requestId` VARCHAR(100) NOT NULL,
  `amount` INT NOT NULL,
  `orderInfo` TEXT,
  `resultCode` INT,
  `message` VARCHAR(255),
  `payType` VARCHAR(50),
  `transId` VARCHAR(100),
  `signature` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `baohanh` (
  `MaBaoHanh` INT NOT NULL AUTO_INCREMENT,
  `MaHoaDon` INT NOT NULL,
  `MaSanPham` INT NOT NULL,
  `NgayMua` DATE NOT NULL,
  `ThoiGianBaoHanh` INT NOT NULL, -- đơn vị: tháng
  `NgayHetHan` DATE NOT NULL,
  `TrangThai` VARCHAR(50) DEFAULT 'Đang bảo hành',
  PRIMARY KEY (`MaBaoHanh`),
  FOREIGN KEY (`MaHoaDon`) REFERENCES `hoadon`(`MaHoaDon`),
  FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham`(`MaSanPham`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `khachhang` (`MaKhachHang`, `HoTen`, `GioiTinh`, `NgaySinh`, `Email`, `SoDienThoai`) VALUES
(1, 'Nguyễn Văn D', 'Nam', '1990-05-11', 'a.@.gmail.com', '0901234545'),
(3, 'Nguyễn Thị B', 'Nữ', '0000-00-00', '', '1234567890'),
(5, 'lamvanc', 'Nam', '1997-03-05', 'lamvanc@gmail.com', '0939049151'),
(6, 'Trần Thị Mai', 'Nữ', '1995-08-21', 'maitran@gmail.com', '0908123456'),
(7, 'Lê Văn Hùng', 'Nam', '1988-12-10', 'hunglv@gmail.com', '0912345678'),
(8, 'Phạm Quỳnh Anh', 'Nữ', '1992-07-15', 'quynhanh.pham@gmail.com', '0923456789'),
(9, 'Ngô Đức Thắng', 'Nam', '1990-01-22', 'thang.ngo@gmail.com', '0934567890'),
(10, 'Đỗ Thị Hồng', 'Nữ', '1994-06-30', 'hong.do@gmail.com', '0945678901'),
(11, 'Bùi Minh Nhật', 'Nam', '1996-11-11', 'nhat.bui@gmail.com', '0956789012'),
(12, 'Hoàng Thị Kim', 'Nữ', '1991-04-18', 'kimhoang@gmail.com', '0967890123'),
(13, 'Nguyễn Văn Tài', 'Nam', '1987-09-25', 'tai.nguyen@gmail.com', '0978901234'),
(14, 'Trịnh Mỹ Duyên', 'Nữ', '1993-03-09', 'myduyen.trinh@gmail.com', '0989012345'),
(15, 'Lâm Quốc Bảo', 'Nam', '1999-12-05', 'baolam@gmail.com', '0990123456');

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`MaLoai`, `TenLoai`) VALUES
(1, 'Laptop văn phòng'),
(2, 'Laptop Gaming');

--
-- Đang đổ dữ liệu cho bảng `Thuonghieu`
--
INSERT INTO `Thuonghieu` (`mathuonghieu`, `tenthuonghieu`) VALUES
(1, 'ASUS'),
(2, 'MSI'),
(3, 'Dell'),
(4, 'HP'),
(5, 'Apple'),
(6, 'Acer'),
(7, 'Lenovo');

--
-- Đang đổ dữ liệu cho bảng `mausac`
--

INSERT INTO `mausac` (`MaMauSac`, `TenMauSac`, `MaSanPham`) VALUES
(1, 'Đen', 1),
(2, 'Trắng', 2);

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`MaSanPham`, `TenSanPham`, `MaLoaiSanPham`, `mathuonghieu`, `CPU`, `RAM`, `CardManHinh`, `SSD`, `ManHinh`, `MaMauSac`, `Gia`, `SoLuong`, `MoTa`, `TrangThai`) VALUES
(1, 'ASUS Vivobook Go 15 E1504FA', 1, 1, 'AMD Ryzen 5 7520U', '16GB LPDDR5', 'AMD Radeon Graphics', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080)', 1, 12500000,3, 'Laptop mỏng nhẹ, phù hợp cho công việc văn phòng và học tập, thiết kế hiện đại, thời lượng pin khá.', 1),
(2, 'Dell Inspiron 15 3520', 1, 3, 'Intel Core i5-1235U', '8GB DDR4', 'Intel Iris Xe Graphics', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080) 120Hz', 1, 15000000, 3, 'Laptop phổ thông, hiệu năng ổn định cho các tác vụ hàng ngày, thiết kế bền bỉ, màn hình 120Hz mượt mà.', 1),
(3, 'HP 15-fc0085AU R5', 1, 4, 'AMD Ryzen 5 7520U', '8GB LPDDR5', 'AMD Radeon Graphics', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080)', 1, 11500000, 3, 'Laptop mỏng nhẹ, hiệu năng tốt cho công việc văn phòng, học tập và giải trí cơ bản, sử dụng vi xử lý AMD tiết kiệm năng lượng.', 1),
(4, 'MacBook Air 13 inch M4 (Dự kiến)',1,5, 'Apple M4 chip', '8GB unified memory', 'Apple M4 GPU (dự kiến)', '256GB SSD', '13.6 inch Liquid Retina', 1, 32000000, 3, 'Dự kiến: Thiết kế siêu mỏng nhẹ, hiệu năng vượt trội với chip M4 mới, tập trung vào AI, thời lượng pin dài, màn hình đẹp. Thông số cần xác nhận khi ra mắt.', 1),
(5, 'Acer Aspire 7 A715-76G', 2, 1, 'Intel Core i5-12450H', '8GB DDR4', 'NVIDIA GeForce RTX 2050 4GB', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080) 144Hz', 1, 17000000, 3, 'Laptop gaming tầm trung, hiệu năng khá cho công việc, học tập và chơi game ở mức cài đặt trung bình. Màn hình 144Hz.', 1),
(6, 'Lenovo IdeaPad Slim 3 15IRU8', 1, 7, 'Intel Core i5-1335U', '16GB DDR4', 'Intel Iris Xe Graphics', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080)', 1, 14000000,3, 'Laptop mỏng nhẹ, giá cả phải chăng, phù hợp cho sinh viên và nhân viên văn phòng với các tác vụ cơ bản hàng ngày.',1),
(7, 'Lenovo LOQ 15IAX9E', 2, 7, 'Intel Core i5-12450HX', '8GB DDR5', 'NVIDIA GeForce RTX 2050 4GB', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080) 144Hz', 1, 19000000, 3, 'Laptop gaming tầm trung, hiệu năng tốt, thiết kế lấy cảm hứng từ dòng Legion, tản nhiệt cải tiến, màn hình 144Hz.', 1),
(8, 'MacBook Air 13 inch M1 (2020)', 1, 5, 'Apple M1 chip (8-core CPU)', '8GB unified memory', 'Apple M1 GPU (7-core)', '256GB SSD', '13.3 inch Retina (2560x1600)', 1, 18500000, 3, 'Laptop siêu mỏng nhẹ, hiệu năng ấn tượng với chip M1, thời lượng pin cực dài, hoạt động mát mẻ.', 1),
(9, 'MSI Modern 15 B13M', 1, 2, 'Intel Core i7-1355U', '16GB DDR4', 'Intel Iris Xe Graphics', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080) IPS', 1, 16500000, 3, 'Laptop mỏng nhẹ, thiết kế thanh lịch, hướng đến người dùng văn phòng, sinh viên cần sự di động và hiệu năng ổn định.', 1),
(10, 'MSI Thin 15 B13VE', 2, 2, 'Intel Core i5-13420H', '8GB DDR4', 'NVIDIA GeForce RTX 4050 6GB', '512GB PCIe NVMe SSD', '15.6 inch FHD (1920x1080) 144Hz', 1, 20000000, 3, 'Laptop gaming có thiết kế tương đối mỏng và nhẹ, cân bằng giữa tính di động và hiệu năng chơi game với card RTX 4050.', 1);

--
-- Đang đổ dữ liệu cho bảng `diachi`
--

INSERT INTO `diachi` (`MaDiaChi`, `ThongTinDiaChi`, `TenNguoiNhan`, `SoDienThoai`, `MaKhachHang`, `MacDinh`) VALUES
(1, 'Số 102 ấp Đông B', 'Anh B', '0901234545', 1, 1),
(6, '1250 Huynh Tan Phat  Q7', 'Nguyen Thi B', '0909909890', 1, 1),
(7, '1250 huynh tan phat', 'Nguyen Thi B', '0918836151', 3, 1);

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`MaHoaDon`, `MaKhachHang`, `NgayDatHang`, `MaDiaChi`, `TongTien`, `PhuongThucThanhToan`, `TrangThai`) VALUES
(19, 3, '2025-01-16', 7, 60010000, 'Thanh toán khi nhận hàng', '6'),
(20, 1, '2025-01-16', 1, 30020000, 'Thanh toán khi nhận hàng', '4'),
(21, 1, '2025-01-16', 1, 18010000, 'Thanh toán khi nhận hàng', '6'),
(22, 3, '2025-01-16', 7, 146460000, 'Thanh toán khi nhận hàng', '4'),
(23, 3, '2025-01-16', 7, 17420000, 'Chuyển khoản ngân hàng', '6');

--
-- Đang đổ dữ liệu cho bảng `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`MaChiTietHoaDon`, `MaHoaDon`, `MaSanPham`, `SoLuong`, `DonGia`, `GiamGia`) VALUES
(31, 19, 1, 2, 29990000, 0),
(32, 20, 1, 1, 29990000, 0),
(33, 21, 2, 2, 8990000, 0),
(34, 22, 1, 2, 29990000, 0),
(35, 22, 3, 5, 17290000, 0),
(36, 23, 4, 1, 17390000, 0);

--
-- Đang đổ dữ liệu cho bảng `binhluandanhgia`
--

INSERT INTO `binhluandanhgia` (`MaBinhLuan`, `MaKhachHang`, `MaSanPham`, `MaHoaDon`, `SoSao`, `NoiDung`, `NgayDanhGia`, `TrangThai`) VALUES
(1, 3, 1, 19, 5, 'Laptop ASUS Vivobook Go 15 E1504FA rất mỏng nhẹ, phù hợp cho công việc văn phòng và học tập, thiết kế hiện đại, thời lượng pin khá.', '2025-01-16 08:15:00', 1),
(2, 1, 2, 20, 4, 'Laptop Dell Inspiron 15 3520 có hiệu năng ổn định, thiết kế bền bỉ, màn hình 120Hz mượt mà.', '2025-01-16 08:15:00', 1),
(3, 1, 3, 21, 5, 'Laptop HP 15-fc0085AU R5 có hiệu năng tốt cho công việc văn phòng, học tập và giải trí cơ bản.', '2025-01-16 08:15:00', 1),
(4, 3, 1, 22, 5, 'Laptop ASUS Vivobook Go 15 E1504FA có thiết kế hiện đại, thời lượng pin khá.', '2025-01-16 08:15:00', 1),
(5, 3, 3, 22, 4, 'Laptop ASUS Vivobook Go 15 E1504FA có hiệu năng tốt cho công việc văn phòng, học tập và giải trí cơ bản.', '2025-01-16 08:15:00', 1),
(6, 3, 4, 23, 5, 'Laptop Dell Inspiron 15 3520 có hiệu năng tốt cho công việc văn phòng, học tập và giải trí cơ bản.', '2025-01-16 08:15:00', 1);

--
-- Đang đổ dữ liệu cho bảng `giohang`
--

INSERT INTO `giohang` (`MaGioHang`, `MaKhachHang`, `MaSanPham`, `SoLuong`, `TrangThai`) VALUES
(1, 3, 1, 2, 1),
(2, 1, 2, 1, 1),
(3, 1, 3, 2, 1),
(4, 3, 1, 2, 1),
(5, 3, 3, 5, 1),
(6, 3, 4, 1, 1);

--
-- Đang đổ dữ liệu cho bảng `hinhanh`
--

INSERT INTO `hinhanh` (`MaHinhAnh`, `DuongDan`, `MacDinh`, `MaSanPham`) VALUES
(1, 'https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg', 1, 1),
(2, 'https://i.postimg.cc/jqBXJnM3/asus-vivobook-go15.jpg', 0, 1),
(3, 'https://i.postimg.cc/rwNCbnM7/asus-vivobook-go15.jpg', 0, 1),
(4, 'https://i.postimg.cc/X7FgKNhj/asus-vivobook-go15.jpg', 0, 1),
(5, 'https://i.postimg.cc/Bn0Bm99x/dell-inspiron-15-3520.jpg', 1, 2),
(6, 'https://i.postimg.cc/q7mcBNtL/dell-inspiron-15-3520.jpg', 0, 2),
(7, 'https://i.postimg.cc/fLJcVMmh/dell-inspiron-15-3520.jpg', 0, 2),
(8, 'https://i.postimg.cc/wBxcdXBM/dell-inspiron-15-3520.jpg', 0, 2),
(9, 'https://i.postimg.cc/NMS1TZk2/hp-15-fc0085au-r5.jpg', 1, 3),
(10, 'https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg', 0, 3),
(11, 'https://i.postimg.cc/RhtwTGgs/hp-15-fc0085au-r5.jpg', 0, 3),
(12, 'https://i.postimg.cc/dQZRvtJ2/hp-15-fc0085au-r5.jpg', 0, 3),
(13, 'https://i.postimg.cc/52b8wBLg/macbook-air-m4.jpg', 1, 4),
(14, 'https://i.postimg.cc/VNHMQ2TZ/macbook-air-m4.jpg', 0, 4),
(15, 'https://i.postimg.cc/6pcnz8hX/macbook-air-m4.jpg', 0, 4),
(16, 'https://i.postimg.cc/kXxx616D/macbook-air-m4.jpg', 0, 4),
(17, 'https://i.postimg.cc/ZnZ4n5vf/acer-aspire7-a715.jpg', 1, 5),
(18, 'https://i.postimg.cc/43DJC5fR/acer-aspire7-a715.jpg', 0, 5),
(19, 'https://i.postimg.cc/zBTqqgvb/acer-aspire7-a715.jpg', 0, 5),
(20, 'https://i.postimg.cc/XvGVDh0g/acer-aspire7-a715.jpg', 0, 5),
(21, 'https://i.postimg.cc/fRkZJFkh/lenovo-ideapad-slim3.jpg', 1, 6),
(22, 'https://i.postimg.cc/1t1Z73B9/lenovo-ideapad-slim3.jpg', 0, 6),
(23, 'https://i.postimg.cc/RZ8mfRnJ/lenovo-ideapad-slim3.jpg', 0, 6),
(24, 'https://i.postimg.cc/wBmpkmhf/lenovo-ideapad-slim3.jpg', 0, 6),
(25, 'https://i.postimg.cc/tJ2QgtjT/lenovo-loq-15iax9e.jpg', 1, 7),
(26, 'https://i.postimg.cc/TwjvzvNn/lenovo-loq-15iax9e.jpg', 0, 7),
(27, 'https://i.postimg.cc/WpnPSjkv/lenovo-loq-15iax9e.jpg', 0, 7),
(28, 'https://i.postimg.cc/5yxW1KDT/lenovo-loq-15iax9e.jpg', 0, 7),
(29, 'https://i.postimg.cc/28kCf3Dy/macbook-air-m1.jpg', 1, 8),
(30, 'https://i.postimg.cc/tJ5yxGh9/macbook-air-m1.jpg', 0, 8),
(31, 'https://i.postimg.cc/c4F01Zxm/macbook-air-m1.jpg', 0, 8),
(32, 'https://i.postimg.cc/xjy97MBB/macbook-air-m1.jpg', 0, 8),
(33, 'https://i.postimg.cc/RVZxD87n/msi-modern-15.jpg', 1, 9),
(34, 'https://i.postimg.cc/YCJcjCJ1/msi-modern-15.jpg', 0, 9),
(35, 'https://i.postimg.cc/PrVns4yV/msi-modern-15.jpg', 0, 9),
(36, 'https://i.postimg.cc/0y6R8wHk/msi-modern-15.jpg', 0, 9),
(37, 'https://i.postimg.cc/MTShbrZH/msi-thin-15.jpg', 1, 10),
(38, 'https://i.postimg.cc/G2x6MJ9Q/msi-thin-15.jpg', 0, 10),
(39, 'https://i.postimg.cc/Dzs9GKTR/msi-thin-15.jpg', 0, 10),
(40, 'https://i.postimg.cc/x8jhBJzS/msi-thin-15.jpg', 0, 10);

--
-- Đang đổ dữ liệu cho bảng `Sanphamyeuthich`
--

INSERT INTO `Sanphamyeuthich` (`id`, `MaSanPham`, `MaKhachHang`) VALUES
(1, 1, 3),
(2, 2, 1),
(3, 3, 1),
(4, 4, 3),
(5, 5, 3),
(6, 6, 1),
(7, 7, 1),
(8, 8, 3),
(9, 9, 3),
(10, 10, 3);

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`TenTaiKhoan`, `MaKhachHang`, `MatKhau`, `LoaiTaiKhoan`, `TrangThai`) VALUES
('lamvanc', 5, '123', 0, 1),
('nguyenthib', 3, '1234', 0, 1),
('nguyenvana', 1, '12345678', 1, 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
--
-- Chỉ mục cho bảng `Sanphamyeuthich`
--
ALTER TABLE `Sanphamyeuthich` 
  ADD KEY `FK_Sanphamyeuthich_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_Sanphamyeuthich_SanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD KEY `FK_ChiTietHoaDon_HoaDon` (`MaHoaDon`),
  ADD KEY `FK_ChiTietHoaDon_SanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `diachi`
--
ALTER TABLE `diachi`
  ADD KEY `fk_diachi_khachhang` (`MaKhachHang`);

--
-- Chỉ mục cho bảng `giohang`
--
ALTER TABLE `giohang`
  ADD KEY `FK_GioHang_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_GioHang_SanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `hinhanh`
--
ALTER TABLE `hinhanh`
  ADD KEY `fk_ma_sanpham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD KEY `FK_HoaDon_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_HoaDon_DiaChi` (`MaDiaChi`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKhachHang`),
  ADD UNIQUE KEY `Email` (`Email`);


--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`TenTaiKhoan`),
  ADD KEY `FK_TaiKhoan_KhachHang` (`MaKhachHang`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `Thuonghieu` 
--
ALTER TABLE `Thuonghieu` MODIFY `mathuonghieu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT cho bảng `Sanphamyeuthich` 
--
ALTER TABLE `Sanphamyeuthich` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
--
-- AUTO_INCREMENT cho bảng `binhluandanhgia`
--
ALTER TABLE `binhluandanhgia`
  MODIFY `MaBinhLuan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  MODIFY `MaChiTietHoaDon` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT cho bảng `diachi`
--
ALTER TABLE `diachi`
  MODIFY `MaDiaChi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `giohang`
--
ALTER TABLE `giohang`
  MODIFY `MaGioHang` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `MaSanPham` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các khóa ngoại và chỉ mục khác
--
ALTER TABLE `sanpham`
  ADD KEY `FK_SanPham_LoaiSanPham` (`MaLoaiSanPham`),
  ADD KEY `FK_SanPham_Thuonghieu` (`mathuonghieu`),
  ADD KEY `FK_SanPham_MauSac` (`MaMauSac`);

ALTER TABLE `binhluandanhgia`
  ADD KEY `FK_BinhLuanDanhGia_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_BinhLuanDanhGia_SanPham` (`MaSanPham`),
  ADD KEY `FK_BinhLuanDanhGia_HoaDon` (`MaHoaDon`);

  -- Thêm index cho otp
ALTER TABLE `otp`
  ADD KEY `IDX_Otp_TenTaiKhoan` (`TenTaiKhoan`);

-- Thêm index cho tin_nhan
ALTER TABLE `tin_nhan`
  ADD KEY `IDX_TinNhan_MaKhachHang` (`MaKhachHang`);

-- Thêm index cho giaodich_momo
ALTER TABLE `giaodich_momo`
  ADD KEY `IDX_GiaoDichMoMo_MaHoaDon` (`MaHoaDon`);

