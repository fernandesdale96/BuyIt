<?php


require_once __DIR__ . '/db_connect.php';

$db = new DB_Connect();
 
// json response array
$response = array();
 
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // check if user is already existed with the same email
    $result = mysql_query("SELECT * FROM users WHERE email = $email");

    if ($result) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $uuid = uniqid('', true);
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        $stored_salt = $hash["salt"];


        $test = mysql_query("INSERT INTO users(UNIQUE_ID,NAME,EMAIL,ENCRYPTED_PASSWORD,SALT,CREATED_AT) VALUES($uuid,$name,$email,$encrypted,$stored_salt)");
        
        $user = mysql_query("SELECT * FROM users WHERE email=$email") or die(mysql_error());
        

        //$user = $db->storeUser($name, $email, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>