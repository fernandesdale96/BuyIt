<?php


$connection_r = mysqli_connect("localhost","root","","products");
header('Content-Type: application/json');



// get all products from basket table
$result = mysqli_query($connection_r,"SELECT * FROM cards");

if($result){
	$response["cards"] =  array();

	while($row = mysqli_fetch_array($result)){
		$product = array();
		$product["cnum"] = $row["ccnum"];
		$product["email"] = $row["email"];

		array_push($response["cards"],$product);


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