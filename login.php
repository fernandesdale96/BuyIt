<?php

	$connection_r = mysqli_connect("localhost","root","","products");

	header('Content-Type: application/json');

	$uemail = "";
	$upass = "";

	if(isset($_POST['password'])){
		$upass = $_POST['password'];
	}

	if(isset($_POST['email'])){
		$uemail = $_POST['email'];
	}

	$hashed_password = md5($upass);


	$json_login = loginUser($uemail,$hashed_password,$connection_r);
	echo json_encode($json_login);


	function isValidEmail($email){
    	return filter_var($email,FILTER_VALIDATE_EMAIL) !== false;
    }

    function loginUser($email,$hashed_password,$connection_r){
    	$exists = false;
    	$isValid = isValidEmail($email);
    	if($isValid){
	    	$result_m = mysqli_query($connection_r,"SELECT * FROM users WHERE email = '$email'");
	    	$temp_array = mysqli_fetch_array($result_m);
	    	$num = count($temp_array);
	    	

	    	if($num >0){
	    		$exists = true;
	    	}
	    	
	    	if($exists){
	    		$json['success'] = true;
	    		$json['message'] = "User Logged in Sucessfully";
	    	}
	    	else{
	    		$json['success'] = false;
	    		$json['message'] = "User does not exist, Please Signup";
	    	}	
	    }
	    else{
	    	$json['success'] = false;
	    	$json['message'] = "Please enter valid email";
	    }
	    return $json;
    }


?>