<?php

require_once __DIR__ . '/db_connect.php';

$db = new DB_Connect();
 
// json response array
$response = array();
 
if (isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // get the user by email and password
    $user = mysql_query('SELECT * FROM users WHERE email = $email');
    if(mysql_num_rows($user) > 0){
        //check user credentials
        $salt = $user['salt'];
        $encrypted_password = $user['encrypted_password'];
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        if($encrypted_password != $hash){
            $user = NULL;
        }
    }
 
    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>