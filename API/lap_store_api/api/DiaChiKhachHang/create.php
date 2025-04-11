<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With');

include_once('../../config/database.php');
include_once('../../model/diachi.php');
include_once('../../model/diachikhachhang.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp diachi và diachikhachhang
$diachi = new DiaChi($conn);
$diachikhachhang = new DiaChiKhachHang($conn);

// Nhận dữ liệu JSON từ client
$data = json_decode(file_get_contents("php://input"));

// Kiểm tra dữ liệu đầu vào
if (!empty($data->ThongTinDiaChi) && !empty($data->SoDienThoai) && !empty($data->MaKhachHang)) {
    // Thêm thông tin vào bảng diachi
    $diachi->ThongTinDiaChi = $data->ThongTinDiaChi;
    $diachi->SoDienThoai = $data->SoDienThoai;

    if ($diachi->AddDiaChi()) {
        // Nếu thêm thành công, lấy ID của dòng vừa thêm
        $lastInsertedId = $conn->lastInsertId();

        // Thêm vào bảng diachikhachhang
        $diachikhachhang->MaDiaChi = $lastInsertedId;
        $diachikhachhang->MaKhachHang = $data->MaKhachHang;

        if ($diachikhachhang->AddDiaChiKhachHang()) {
            echo json_encode(array('message' => 'Dia Chi Khach Hang Created'));
        } else {
            echo json_encode(array('message' => 'Dia Chi Created but Khach Hang Association Failed'));
        }
    } else {
        echo json_encode(array('message' => 'Dia Chi Not Created'));
    }
} else {
    echo json_encode(array('message' => 'Incomplete Input Data'));
}
?>
