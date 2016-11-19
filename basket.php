<?php


	$connection_r = mysqli_connect("localhost","root","","products");
	header('Content-Type: application/json');

	$pname = "";
	$price = "";
	$description ="";
	$location = "";

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

	$json_addbasket = addBasket($pname,$price,$description,$location,$connection_r);

	echo json_encode($json_addbasket);


	function addBasket($pname,$price,$description,$location,$connection_r){
	
		
			$result_m = mysqli_query($connection_r,"INSERT INTO basket(name, price, location, description) VALUES('$pname', '$price', '$location', '$description')");

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