<?php


	$connection_r = mysqli_connect("localhost","root","","products");
	header('Content-Type: application/json');

	$pname = "";
	$price = "";
	$location = "";
	$email = "";

	if(isset($_POST['name'])){
		$pname = $_POST['name'];
	}

	if(isset($_POST['price'])){
		$price = $_POST['price'];
	}


	if(isset($_POST['location'])){
		$location = $_POST['location'];
	}
	if(isset($_POST['email'])){
		$email = $_POST['email'];
	}

	$json_addbasket = addPurchase($pname,$price,$location,$email,$connection_r);

	echo json_encode($json_addbasket);


	function addPurchase($pname,$price,$location,$email,$connection_r){
	
		
			$result_m = mysqli_query($connection_r,"INSERT INTO purchased(name, price, location, uemail) VALUES('$pname', '$price', '$location', '$email')");

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