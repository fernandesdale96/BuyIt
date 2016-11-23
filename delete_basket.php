<?php

	$connection_r = mysqli_connect("localhost","root","","products");
	header('Content-Type: application/json');

	$email = "";

	if(isset($_POST["email"])){
		$email = $_POST["email"];
	}

	$json_delete = deleteBasket($connection_r,$email);

	echo json_encode($json_delete);

	function deleteBasket($connection_r,$email){

		$result = mysqli_query($connection_r, "DELETE FROM basket WHERE email = '$email'");

		if($result){
			$json['success'] = true;
			$json['message'] = "Product removed from basket";

		}
		else{
			$json['success'] = false;
			$json['message'] = "Product could not be removed";
		}

		return $json;
	}



?>