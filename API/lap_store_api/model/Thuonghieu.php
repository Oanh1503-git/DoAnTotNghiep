<?php


class Thuonghieu {

    private $conn;


    public $mathuonghieu;
    public $tenthuonghieu;



    public function __construct() {
        global $conn;
        $this->conn = $conn;
    }

    public function getAll() {
        $stmt = $this->conn->query("SELECT * FROM Thuonghieu");
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function getById($mathuonghieu) {
        $stmt = $this->conn->prepare("SELECT * FROM Thuonghieu WHERE mathuonghieu = ?");
        $stmt->execute([$mathuonghieu]);
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    public function create($tenthuonghieu) {
        $stmt = $this->conn->prepare("INSERT INTO Thuonghieu (tenthuonghieu) VALUES (?)");
        return $stmt->execute([$tenthuonghieu]);
    }

    public function update($mathuonghieu, $tenthuonghieu) {
        $stmt = $this->conn->prepare("UPDATE Thuonghieu SET tenthuonghieu = ? WHERE mathuonghieu = ?");
        return $stmt->execute([$tenthuonghieu, $mathuonghieu]);
    }

    public function delete($mathuonghieu) {
        $stmt = $this->conn->prepare("DELETE FROM Thuonghieu WHERE mathuonghieu = ?");
        return $stmt->execute([$mathuonghieu]);
    }
}
?>