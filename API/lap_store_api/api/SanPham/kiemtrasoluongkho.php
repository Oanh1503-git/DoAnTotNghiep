<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/sanpham.php');

// Kết nối CSDL
$database = new Database();
$conn = $database->Connect();

// Khởi tạo đối tượng SanPham
$sanpham = new SanPham($conn);

// Kiểm tra mã sản phẩm có được truyền vào không
if (!isset($_GET['MaSanPham'])) {
    echo json_encode([
        'success' => false,
        'message' => 'Thiếu MaSanPham'
    ]);
    exit;
}

$sanpham->MaSanPham = $_GET['MaSanPham'];

// Truy vấn số lượng tồn kho
try {
    $query = "SELECT SoLuong FROM sanpham WHERE MaSanPham = ?";
    $stmt = $conn->prepare($query);
    $stmt->execute([$sanpham->MaSanPham]);

    $row = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($row) {
        echo json_encode([
            'success' => true,
            'MaSanPham' => $sanpham->MaSanPham,
            'SoLuongTonKho' => (int)$row['SoLuong']
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => 'Không tìm thấy sản phẩm với mã: ' . $sanpham->MaSanPham
        ]);
    }
} catch (PDOException $e) {
    echo json_encode([
        'success' => false,
        'message' => 'Lỗi truy vấn: ' . $e->getMessage()
    ]);
}
?>
