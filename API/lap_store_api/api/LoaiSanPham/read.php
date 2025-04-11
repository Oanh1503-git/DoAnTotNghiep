<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
include_once('../../model/loaisanpham.php');

// Tạo đối tượng database và kết nối
$database = new database();
$conn = $database->Connect(); // Lấy kết nối PDO

// Khởi tạo lớp loaisanpham với kết nối PDO
$loaisanpham = new loaisanpham($conn);

// Lấy tất cả loaisanpham
$getAllLoaiSanPham = $loaisanpham->GetAllLoaiSanPham();

$num = $getAllLoaiSanPham->rowCount();

if($num>0){
    $loaisanpham_array =[];
    $loaisanpham_array['loaisanpham'] =[];

    while($row = $getAllLoaiSanPham->fetch(PDO::FETCH_ASSOC)){
        extract($row);

        $loaisanpham_item = array(
            'MaLoai'=> $MaLoai,
            'TenLoai'=> $TenLoai
        );
        array_push($loaisanpham_array['loaisanpham'],$loaisanpham_item);
    }
    echo json_encode($loaisanpham_array, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>