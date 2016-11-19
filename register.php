<?php
    
    
   
	$connection_r = mysqli_connect("localhost","root","","products");
	header('Content-Type: application/json');
	/*require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();*/

    
    
	$uname = "";
	$uemail = "";
	$upass = "";

	if(isset($_POST['name'])){
		$uname = $_POST['name'];
	}

	if(isset($_POST['password'])){
		$upass = $_POST['password'];
	}

	if(isset($_POST['email'])){
		$uemail = $_POST['email'];
	}

	$hashed_password = md5($upass);


	/*$query = "INSERT INTO users(username,password,email) VALUES ('$uname','$upass','$uemail')";
	$inserted = mysql_query($query);

	$retr = "SELECT * FROM users WHERE email = '$uemail' AND password = '$upass'";
	$something = mysql_query($retr);*/








	$json_registration = registerUser($uname,$hashed_password,$uemail,$connection_r);

	echo json_encode($json_registration);









	//Helper Functions for Registration

    /*function loginExists($email, $password,$connection_r){
    	$result_m = mysqli_query($connection_r,"SELECT * FROM users WHERE email = '$email'");
    	$row = mysqli_fetch_array($result_m);
    	$num = count($row);

    	

    	if($num == 0){
    		return false;
    	}

    	
    	return true;

    	
    }*/

    function isValidEmail($email){
    	return filter_var($email,FILTER_VALIDATE_EMAIL) !== false;
    }

    function registerUser($username,$password,$email,$connection_r){

    	$exists=false;

    	$result_m = mysqli_query($connection_r,"SELECT * FROM users WHERE email = '$email'");

    	$temp_array = mysqli_fetch_array($result_m);
    	$num = count($temp_array);

    	if($num>0){
    		$exists = true;
    	}


    	if($exists){
    		$json['success'] = false;
    		$json['message'] = "Error during registration, User already exists";
    	}

    	else{
    		$isValid = isValidEmail($email);
    		if($isValid){
    			$query = "INSERT INTO users (username,password,email,created_at,updated_at) VALUES ('$username','$password','$email',NOW(),NOW())";
    			
    			$inserted = mysqli_query($connection_r,$query);

    			if($inserted == 1){
    				$json['success'] = true;
    				$json['message'] = "User Registered Sucessfully";
    				
    				/*$result = mysqli_query($connection_r,"SELECT * FROM users WHERE email = '$email' AND password = '$password'");
    				$row = mysqli_fetch_array($result);
    				$response["users"] = array();
    				$users = array();

    				$users["id"] = $row["id"];
            		$users["name"] = $row["username"];
            		$users["password"] = $row["password"];
            		$users["email"] = $row["email"];

            		array_push($response["users"], $users);

    				echo json_encode($response);*/
    			}
    			else{
    				$json['success'] = false;
    				$json['message'] = "Error during registration. User already exists";
    			}
    			mysqli_close($connection_r);


    		}
    		else{
    			$json['success'] = false;
    			$json['message'] = "Error in registering. Email Address is not valid.";
    		}
    	}
    	return $json;
    }



    ?>