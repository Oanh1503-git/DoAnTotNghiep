<?php
header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
require_once '../../model/Thuonghieu.php';

$database = new database();
$conn = $database->Connect();


$input = json_decode(file_get_contents('php://input'), true);
if (!isset($input['mathuonghieu'])) {
    echo json_encode(['success' => false, 'message' => 'Thiếu mã thương hiệu']);
    exit();
}

$thuonghieu = new Thuonghieu($conn);
if ($thuonghieu->delete($input['mathuonghieu'])) {
    echo json_encode(['success' => true, 'message' => 'Xóa thương hiệu thành công']);
} else {
    echo json_encode(['success' => false, 'message' => 'Xóa thương hiệu thất bại']);
}
exit();
?>