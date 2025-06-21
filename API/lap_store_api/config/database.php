<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

class database
{
    private $servername = "localhost";
    private $username = "laptopsh_laptopsh";
    private $password = "i%Mt%VF+#R8=e1#2";
    private $database = "laptopsh_laptopstore";
    private $conn;

    public function Connect()
    {
        $this->conn = null;
        try {
            $this->conn = new PDO(
                "mysql:host=$this->servername;dbname=$this->database;charset=utf8mb4",
                $this->username,
                $this->password
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            echo "Connection failed: " . $e->getMessage();
        }
        return $this->conn;
    }
}
