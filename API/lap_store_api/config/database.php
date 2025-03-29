<?php
// Kết nối database bằng PDO
class database
{
    private $servername = "localhost";
    private $username = "root";
    private $password = "";
    private $database = "quanlybanlaptop";
    private $conn;

    public function Connect()
    {
        $this->conn = null; // Khởi tạo giá trị mặc định cho kết nối
        try {
            // Khởi tạo kết nối PDO và gán vào $this->conn
            $this->conn = new PDO("mysql:host=$this->servername;dbname=$this->database", $this->username, $this->password);
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            
        } catch (PDOException $e) {
            echo "Connection failed: " . $e->getMessage();
        }
        return $this->conn; // Trả về kết nối
    }
}
