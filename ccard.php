<?php
	$connection_r = mysqli_connect("localhost","root","","products");

	header('Content-Type: application/json');

	$cnum = "";
	$uemail="";

	if(isset($_POST['cnum'])){
		$cnum = $_POST['cnum'];
	}

	if(isset($_POST['email'])){
		$uemail = $_POST['email'];
	}

	$json_cc = register_card($cnum,$uemail,$connection_r);
	echo json_encode($json_cc);


	function register_card($cnum,$uemail,$connection_r){
		$exists = false;

		$num_rows = mysqli_query($connection_r,"SELECT * FROM cards WHERE ccnum = '$cnum'");

		$temp_array = mysqli_fetch_array($num_rows);
		$num = count($temp_array);

		if($num>0){
			$exists = true;
		}

		if($exists){
				$json['success'] = false;
    			$json['message'] = "CC Number Already Exists";
		}
		else{
			$result_m = mysqli_query($connection_r,"INSERT INTO cards(ccnum,email) VALUES('$cnum','$uemail')");
			if($result_m){
				$json['success'] = true;
				$json['message'] = "CC added to table";
			}
			else{
				$json['success'] = false;
				$json['message'] = "Some error occured";
			}
		}

		return $json;

	}




?>