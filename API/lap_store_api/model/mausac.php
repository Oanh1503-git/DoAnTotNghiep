<?php
class mausac{
    private $conn;

    //Thuoc tinh
    public $MaMauSac;
    public $TenMauSac;
    //connect db

    public function __construct($database){
        $this->conn = $database;
    }

    //Doc dữ liệu

    public function GetAllMauSac() {
        $query = "SELECT * FROM mausac"; 
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt; // Trả về PDOStatement
    }
    public function GetMauSacById() {
        $query = "SELECT * FROM mausac WHERE MaMauSac = ? LIMIT 1"; 
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1,$this->MaMauSac);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        $this->MaMauSac = $row['MaMauSac'];
        $this->TenMauSac = $row['TenMauSac'];
    } 

    public function AddMauSac(){
        $query = "INSERT INTO mausac SET TenMauSac =:TenMauSac ,  MaMauSac =:MaMauSac";

        $stmt = $this->conn->prepare($query);

        $this->MaMauSac = htmlspecialchars(strip_tags($this->MaMauSac));
        $this->TenMauSac = htmlspecialchars(strip_tags($this->TenMauSac));


        $stmt->bindParam(':MaMauSac',$this->MaMauSac);
        $stmt->bindParam(':TenMauSac',$this->TenMauSac);
        
        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function UpdateMauSac(){
        $query = "UPDATE mausac SET TenMauSac =:TenMauSac  WHERE MaMauSac=:MaMauSac";

        $stmt = $this->conn->prepare($query);

        $this->MaMauSac = htmlspecialchars(strip_tags($this->MaMauSac));
        $this->TenMauSac = htmlspecialchars(strip_tags($this->TenMauSac));

        $stmt->bindParam(':MaMauSac',$this->MaMauSac);
        $stmt->bindParam(':TenMauSac',$this->TenMauSac);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }

    public function deleteMauSac(){
        $query = "DELETE FROM mausac WHERE MaMauSac=:MaMauSac";

        $stmt = $this->conn->prepare($query);

        $this->MaMauSac = htmlspecialchars(strip_tags($this->MaMauSac));

        $stmt->bindParam(':MaMauSac',$this->MaMauSac);

        if($stmt->execute()){
            return true;
        }
        printf("Error %s.\n",$stmt->error);
        return false;
    }
}
?>