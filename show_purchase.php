<?php 
$connection_r = mysqli_connect("localhost","root","","products");
header('Content-Type: application/json');



// get all products from basket table
$result = mysqli_query($connection_r,"SELECT * FROM purchased");

if($result){
    $response["products"] =  array();

    while($row = mysqli_fetch_array($result)){
        $product = array();
        $product["pid"] = $row["pid"];
        $product["email"] = $row["uemail"];
        $product["name"] = $row["name"];
        $product["price"] = $row["price"];
        $product["location"] = $row["location"];



        array_push($response["products"],$product);


    }

    $response["success"] = true;

    echo json_encode($response);
}
else{
    $response["success"] = false;
    $response["message"] = "No Credit Cards Found";

    echo json_encode($response);
}
?>