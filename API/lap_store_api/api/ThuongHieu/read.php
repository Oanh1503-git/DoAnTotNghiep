<?php

header('Access-Control-Allow-Origin:*');
header('Content-Type: application/json');

include_once('../../config/database.php');
require_once '../../model/Thuonghieu.php';

$database = new database();
$conn = $database->Connect();

$thuonghieu = new Thuonghieu($conn);

if (isset($_GET['mathuonghieu'])) {
    $data = $thuonghieu->getById($_GET['mathuonghieu']);
    if ($data) {
        echo json_encode(['success' => true, 'data' => [$data]]);
    } else {
        echo json_encode(['success' => false, 'message' => 'Không tìm thấy thương hiệu']);
    }
} else {
    $data = $thuonghieu->getAll();
    echo json_encode(['success' => true, 'data' => $data]);
}
exit();
?>