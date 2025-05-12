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
-- Cơ sở dữ liệu: `laptopstore`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `binhluandanhgia`
--

CREATE TABLE `binhluandanhgia` (
  `MaBinhLuan` int(11) NOT NULL,
  `MaKhachHang` int(11) NOT NULL,
  `MaSanPham` int(11) NOT NULL,
  `MaHoaDonBan` int(11) NOT NULL,
  `SoSao` int(11) NOT NULL,
  `NoiDung` text DEFAULT NULL,
  `NgayDanhGia` datetime NOT NULL,
  `TrangThai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadonban`
--

CREATE TABLE `chitiethoadonban` (
  `MaChiTietHoaDonBan` int(11) NOT NULL,
  `MaHoaDonBan` int(11) DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL,
  `DonGia` int(20) DEFAULT NULL,
  `ThanhTien` int(20) GENERATED ALWAYS AS (`SoLuong` * `DonGia`) STORED,
  `GiamGia` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiethoadonban`
--

INSERT INTO `chitiethoadonban` (`MaChiTietHoaDonBan`, `MaHoaDonBan`, `MaSanPham`, `SoLuong`, `DonGia`, `GiamGia`) VALUES
(31, 19, 1, 2, 29990000, 0),
(32, 20, 1, 1, 29990000, 0),
(33, 21, 2, 2, 8990000, 0),
(34, 22, 1, 2, 29990000, 0),
(35, 22, 3, 5, 17290000, 0),
(36, 23, 4, 1, 17390000, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `diachi`
--

CREATE TABLE `diachi` (
  `MaDiaChi` int(11) NOT NULL,
  `ThongTinDiaChi` varchar(200) NOT NULL,
  `TenNguoiNhan` varchar(100) NOT NULL,
  `SoDienThoai` char(10) NOT NULL,
  `MaKhachHang` int(11) NOT NULL,
  `MacDinh` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `diachi`
--

INSERT INTO `diachi` (`MaDiaChi`, `ThongTinDiaChi`, `TenNguoiNhan`, `SoDienThoai`, `MaKhachHang`, `MacDinh`) VALUES
(1, 'Số 102 ấp Đông B', 'Anh B', '0901234545', 1, 1),
(6, '1250 Huynh Tan Phat  Q7', 'Nguyen Thi B', '0909909890', 1, 1),
(7, '1250 huynh tan phat', 'Nguyen Thi B', '0918836151', 3, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `giohang`
--

CREATE TABLE `giohang` (
  `MaGioHang` int(11) NOT NULL,
  `MaKhachHang` int(11) DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL,
  `TrangThai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hinhanh`
--

CREATE TABLE `hinhanh` (
  `MaHinhAnh` int(11) NOT NULL,
  `DuongDan` varchar(500) DEFAULT NULL,
  `MacDinh` int(1) NOT NULL,
  `MaSanPham` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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


-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadonban`
--

CREATE TABLE `hoadonban` (
  `MaHoaDonBan` int(11) NOT NULL,
  `MaKhachHang` int(11) DEFAULT NULL,
  `NgayDatHang` date NOT NULL,
  `MaDiaChi` int(11) DEFAULT NULL,
  `TongTien` int(11) DEFAULT NULL,
  `PhuongThucThanhToan` varchar(50) NOT NULL,
  `TrangThai` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadonban`
--

INSERT INTO `hoadonban` (`MaHoaDonBan`, `MaKhachHang`, `NgayDatHang`, `MaDiaChi`, `TongTien`, `PhuongThucThanhToan`, `TrangThai`) VALUES
(19, 3, '2025-01-16', 7, 60010000, 'Thanh toán khi nhận hàng', '6'),
(20, 1, '2025-01-16', 1, 30020000, 'Thanh toán khi nhận hàng', '4'),
(21, 1, '2025-01-16', 1, 18010000, 'Thanh toán khi nhận hàng', '6'),
(22, 3, '2025-01-16', 7, 146460000, 'Thanh toán khi nhận hàng', '4'),
(23, 3, '2025-01-16', 7, 17420000, 'Chuyển khoản ngân hàng', '6');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKhachHang` int(11) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `GioiTinh` char(3) DEFAULT NULL,
  `NgaySinh` date DEFAULT NULL,
  `Email` varchar(100) NOT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`MaKhachHang`, `HoTen`, `GioiTinh`, `NgaySinh`, `Email`, `SoDienThoai`) VALUES
(1, 'Nguyễn Văn D', 'Nam', '1990-05-11', 'a.@.gmail.com', '0901234545'),
(3, 'Nguyễn Thị B', 'Nữ', '0000-00-00', '', '1234567890'),
(5, 'lamvanc', 'Nam', '1997-03-05', 'lamvanc@gmail.com', '0939049151');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `MaLoai` int(11) NOT NULL,
  `TenLoai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`MaLoai`, `TenLoai`) VALUES
(1, 'Laptop văn phòng'),
(2, 'Laptop Gaming');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `mausac`
--

CREATE TABLE `mausac` (
  `MaMauSac` int(11) NOT NULL,
  `TenMauSac` varchar(20) DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `mausac`
--

INSERT INTO `mausac` (`MaMauSac`, `TenMauSac`, `MaSanPham`) VALUES
(1, 'Đen', 1),
(2, 'Trắng', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `MaSanPham` int(11) NOT NULL,
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
  `TrangThai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`MaSanPham`, `TenSanPham`, `MaLoaiSanPham`, `CPU`, `RAM`, `CardManHinh`, `SSD`, `ManHinh`, `MaMauSac`, `Gia`, `SoLuong`, `MoTa`, `TrangThai`) VALUES
(1, 'Laptop gaming ASUS TUF Gaming F15 FX507VV LP304W', 2, 'INTEL CORE I9 12900K', 'RAM 8 GB', 'RTX 4060', 'SSD 512 GB', '1920x1080', 2, 29990000, 2, 'Laptop gaming RTX 40 Series mạnh nhất cho học sinh, sinh viên\n\nHoạt động bền bỉ, làm chủ trận chiến trên chiếc laptop ASUS TUF Gaming F15 (2023), sử dụng bộ vi xử lý Intel Core™ i7-13620H với sự kết hợp giữa lõi hiệu năng (P-core) và lõi tiết kiệm điện (E-core) cân bằng sức mạnh chơi game và khả năng đa nhiệm tuyệt vời. ASUS TUF Gaming F15 còn được trang bị đồ họa NVIDIA GeForce RTX™ 40 series mới nhất hỗ trợ G-SYNC® và có TGP tối đa là 140 W với Dynamic Boost. Sự kết hợp giữa CPU mạnh mẽ và GPU hiệu suất cao mang lại khả năng chơi game đáng kinh ngạc trên cả các tựa game mới nhất. Màn hình FHD 144Hz cho trải nghiệm gaming mượt mà, tận hưởng không gian giải trí đắm chìm. ', 1),
(2, 'Laptop MSI Modern 14 C11M 011VN', 1, 'Intel Core i3-1115G4 (up to 4.1Ghz, 6MB)', 'RAM 8 GB', 'Intel UHD Graphics', 'SSD 512 GB', '1920x1080', 1, 8990000, 20, 'MSI Modern 14 C11M-011VN nằm trong phân khúc laptop 9 triệu được trang bị màn hình rộng 14 inch có độ phân giải Full HD cùng tần số quét ở mức cơ bản 60Hz. Màn hình IPS này sẽ đủ sắc nét để người dùng có thể thoải mái sử dụng trong học tập, nghiên cứu và làm việc.', 1),
(3, 'Laptop gaming ASUS Vivobook 16X K3605ZF RP634W', 2, 'AMD R77700M', 'RAM 8 GB', 'RTX 3050', 'SSD 1TB', '1920x1080', 1, 17290000, 5, 'Bên trong ASUS Gaming Vivobook K3605ZF-RP634W là bộ vi xử lý Intel Core i5-12500H với 4 nhân hiệu năng cao và 8 nhân tiết kiệm điện, cùng với bộ nhớ đệm 18MB, đạt tốc độ tối đa lên đến 4.5 GHz. Cấu hình này không chỉ đáp ứng tốt các tựa game phổ biến mà còn tối ưu cho các tác vụ đa nhiệm và xử lý đồ họa nặng.', 1),
(4, 'Laptop ASUS Vivobook 14 OLED A1405VA KM095W', 1, 'INTEL CORE I7 12500H', 'RAM 8 GB', 'INTEL ARISXE', 'SSD 1TB', '1920x1080', 1, 17390000, 5, 'Tỏa sáng với cả thế giới cùng ASUS Vivobook 14 OLED mạnh mẽ, chiếc laptop tích hợp nhiều tính năng với màn hình OLED rực rỡ, gam màu DCI-P3 đẳng cấp điện ảnh. Mọi thứ trở nên dễ dàng hơn nhờ những tiện ích thân thiện với người dùng bao gồm bản lề duỗi thẳng 180°, nắp che webcam vật lý và các phím chức năng chuyên dụng. Bảo vệ sức khỏe an toàn với ASUS kháng khuẩn Guard Plus trên các bề mặt thường xuyên chạm vào. Bắt đầu ngày mới đầy hứng khởi với ASUS Vivobook 14 OLED!', 1);

-- --------------------------------------------------------
--
-- Cấu trúc bảng cho bảng `Thuonghieu`
--
CREATE TABLE `Thuonghieu` (
  `tenthuonghieu` varchar(50) NOT NULL,
  `mathuonghieu` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- --------------------------------------------------------
--
-- Cấu trúc bảng cho bảng `Sanphamyeuthich` 
--
CREATE TABLE `Sanphamyeuthich` (
  `id` int(11) NOT NULL,
  `MaSanPham` int(11) NOT NULL,
  `MaKhachHang` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `TenTaiKhoan` varchar(50) NOT NULL,
  `MaKhachHang` int(11) NOT NULL,
  `MatKhau` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `LoaiTaiKhoan` int(1) NOT NULL,
  `TrangThai` int(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- Chỉ mục cho bảng `binhluandanhgia`
--
ALTER TABLE `binhluandanhgia`
  ADD PRIMARY KEY (`MaBinhLuan`),
  ADD KEY `FK_BinhLuanDanhGia_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_BinhLuanDanhGia_SanPham` (`MaSanPham`),
  ADD KEY `FK_BinhLuanDanhGia_HoaDonBan` (`MaHoaDonBan`);

--
-- Chỉ mục cho bảng `Thuonghieu` 
--
ALTER TABLE `Thuonghieu` 
  ADD PRIMARY KEY (`mathuonghieu`);

--
-- Chỉ mục cho bảng `Sanphamyeuthich`
--
ALTER TABLE `Sanphamyeuthich` 
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_Sanphamyeuthich_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_Sanphamyeuthich_SanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `chitiethoadonban`
--
ALTER TABLE `chitiethoadonban`
  ADD PRIMARY KEY (`MaChiTietHoaDonBan`),
  ADD KEY `FK_ChiTietHoaDonBan_HoaDonBan` (`MaHoaDonBan`),
  ADD KEY `FK_ChiTietHoaDonBan_SanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `diachi`
--
ALTER TABLE `diachi`
  ADD PRIMARY KEY (`MaDiaChi`),
  ADD KEY `fk_diachi_khachhang` (`MaKhachHang`);

--
-- Chỉ mục cho bảng `giohang`
--
ALTER TABLE `giohang`
  ADD PRIMARY KEY (`MaGioHang`),
  ADD KEY `FK_GioHang_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_GioHang_SanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `hinhanh`
--
ALTER TABLE `hinhanh`
  ADD PRIMARY KEY (`MaHinhAnh`),
  ADD KEY `fk_ma_sanpham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `hoadonban`
--
ALTER TABLE `hoadonban`
  ADD PRIMARY KEY (`MaHoaDonBan`),
  ADD KEY `FK_HoaDonBan_KhachHang` (`MaKhachHang`),
  ADD KEY `FK_HoaDonBan_DiaChi` (`MaDiaChi`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKhachHang`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`MaLoai`);

--
-- Chỉ mục cho bảng `mausac`
--
ALTER TABLE `mausac`
  ADD PRIMARY KEY (`MaMauSac`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`MaSanPham`),
  ADD KEY `FK_SanPham_LoaiSanPham` (`MaLoaiSanPham`),
  ADD KEY `FK_SanPham_Thuonghieu` (`mathuonghieu`),
  ADD KEY `FK_SanPham_MauSac` (`MaMauSac`);

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
-- AUTO_INCREMENT cho bảng `chitiethoadonban`
--
ALTER TABLE `chitiethoadonban`
  MODIFY `MaChiTietHoaDonBan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT cho bảng `diachi`
--
ALTER TABLE `diachi`
  MODIFY `MaDiaChi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `giohang`
--
ALTER TABLE `giohang`
  MODIFY `MaGioHang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- AUTO_INCREMENT cho bảng `hinhanh`
--
ALTER TABLE `hinhanh`
  MODIFY `MaHinhAnh` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT cho bảng `hoadonban`
--
ALTER TABLE `hoadonban`
  MODIFY `MaHoaDonBan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `MaKhachHang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `MaLoai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `mausac`
--
ALTER TABLE `mausac`
  MODIFY `MaMauSac` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `MaSanPham` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `diachi`
--
ALTER TABLE `diachi`
  ADD CONSTRAINT `fk_diachi_khachhang` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD CONSTRAINT `FK_TaiKhoan_KhachHang` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

-- Thêm ràng buộc khóa ngoại cho bảng binhluandanhgia
ALTER TABLE `binhluandanhgia`
  ADD CONSTRAINT `FK_BinhLuanDanhGia_KhachHang` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_BinhLuanDanhGia_SanPham` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_BinhLuanDanhGia_HoaDonBan` FOREIGN KEY (`MaHoaDonBan`) REFERENCES `hoadonban` (`MaHoaDonBan`) ON DELETE CASCADE ON UPDATE CASCADE;

-- Thêm ràng buộc khóa ngoại cho bảng Sanphamyeuthich
ALTER TABLE `Sanphamyeuthich`
  ADD CONSTRAINT `FK_Sanphamyeuthich_KhachHang` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`) ON DELETE CASCADE ON UPDATE CASCADE ,
  ADD CONSTRAINT `FK_Sanphamyeuthich_SanPham` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
