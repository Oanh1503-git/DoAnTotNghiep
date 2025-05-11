<?php
    header('Access-Control-Allow-Origin:*');
    header('Content-Type: application/json');

    include_once('../../config/database.php');
    include_once('../../model/giohang.php');

    // Tạo đối tượng database và kết nối
    $database = new database();
    $conn = $database->Connect(); // Lấy kết nối PDO

    // Khởi tạo lớp Giohang với kết nối PDO
    $giohang = new giohang($conn);

    // Lấy mã khách hàng từ URL
    $giohang->MaKhachHang = isset($_GET['MaKhachHang']) ? $_GET['MaKhachHang'] : die();

    // Tính tổng tiền
    $giohang->TinhTongTien();

    // Chuẩn bị dữ liệu trả về
    $giohang_item = array(
        'TongTien' => (int)$giohang->TongTien // Chuyển đổi thành int trước khi trả về
    );
    

    // In kết quả dưới dạng JSON
    echo json_encode($giohang_item, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>
