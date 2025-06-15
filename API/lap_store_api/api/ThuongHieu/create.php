<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
require_once '../../model/Thuonghieu.php';

$database = new database();
$conn = $database->Connect();

$input = json_decode(file_get_contents('php://input'), true);
if (!isset($input['tenthuonghieu'])) {
    echo json_encode(['success' => false, 'message' => 'Thiếu tên thương hiệu']);
    exit();
}

$thuonghieu = new Thuonghieu($conn);
if ($thuonghieu->create($input['tenthuonghieu'])) {
    echo json_encode(['success' => true, 'message' => 'Thêm thương hiệu thành công']);
} else {
    echo json_encode(['success' => false, 'message' => 'Thêm thương hiệu thất bại']);
}
exit();
?>