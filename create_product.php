<?php


    $connection_r = mysqli_connect("localhost","root","","products");
    header('Content-Type: application/json');

/*
    $pname = "a";
    $price = "a";
    $description = "a";
    $location = "a";
    $distance = "a";
    $image = "a";
*/

    if(isset($_POST['name'])){
        $pname = $_POST['name'];
    }

    if(isset($_POST['price'])){
       $price = $_POST['price'];
    }

    if(isset($_POST['description'])){
        $description = $_POST['description'];
    }

    if(isset($_POST['location'])){
        $location = $_POST['location']; 
    }


    if(isset($_POST['image'])){
        $image = $_POST['image'];
    } 

    $json_addproduct = addProduct($pname, $price, $description, $location, $image, $connection_r);

    echo json_encode($json_addproduct);


    function addProduct($pname, $price, $description, $location, $image, $connection_r){
    
        
            $result_m = mysqli_query($connection_r, "INSERT INTO products(name, price, location, description, image) VALUES('$pname', '$price', '$location', '$description', '$image')" );

            if($result_m){
                $json['success'] = true;
                $json['message'] = "Product added to basket";
            }
            else{
                $json['success'] = false;
                $json['message'] = "Product could not be added to basket";
            }

            return $json;
        }
        

        
?>