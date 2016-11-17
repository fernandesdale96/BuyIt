<?php
    
    include_once __DIR__ .'/db_connect.php';
    
    class User{
        
        private $db;
        
        private $db_table = "users";
        
        public function __construct(){
            $this->db = new DB_CONNECT();
        }
        
        public function isLoginExist($username, $password){
            
            $query = "select * from ".$this->db->getDB()." where username = '$username' AND password = '$password'";
            
            $result = mysql_query($this->db->getDB(), $query);
            
            if(mysql_num_rows($result) > 0){
                
                mysql_close($this->db->getDB());
                
                
                return true;
                
            }
            
            mysql_close($this->db->getDB());
            
            return false;
            
        }
        
        public function isEmailUsernameExist($username, $email){
            
            $query = "select * from ".$this->db_table." where username = '$username' AND email = '$email'";
            
            $result = mysql_query($this->db->getDB(), $query);
            
            if(mysql_num_rows($result) > 0){
                
                mysql_close($this->db->getDB());
                
                return true;
                
            }
               
            return false;
            
        }
        
        public function isValidEmail($email){
            return filter_var($email, FILTER_VALIDATE_EMAIL) !== false;
        }
        
        public function createNewRegisterUser($username, $password, $email){
              
            $isExisting = $this->isEmailUsernameExist($username, $email);
            
            if($isExisting){
                
                $json['success'] = 0;
                $json['message'] = "Error in registering. Probably the username/email already exists";
            }
            
            else{
                
            $isValid = $this->isValidEmail($email);
                
                if($isValid)
                {
                $query = "insert into ".$this->db_table." (username, password, email, created_at, updated_at) values ('$username', '$password', '$email', NOW(), NOW())";
                
                $inserted = mysql_query($this->db->getDB(), $query);
                
                if($inserted == 1){
                    
                    $json['success'] = 1;
                    $json['message'] = "Successfully registered the user";
                    
                }else{
                    
                    $json['success'] = 0;
                    $json['message'] = "Error in registering. Probably the username/email already exists";
                    
                }
                
                mysql_close($this->db->getDB());
                }
                else{
                    $json['success'] = 0;
                    $json['message'] = "Error in registering. Email Address is not valid";
                }
                
            }
            
            return $json;
            
        }
        
        public function loginUsers($username, $password){
            
            $json = array();
            
            $canUserLogin = $this->isLoginExist($username, $password);
            
            if($canUserLogin){
                
                $json['success'] = 1;
                $json['message'] = "Successfully logged in";
                
            }else{
                $json['success'] = 0;
                $json['message'] = "Incorrect details";
            }
            return $json;
        }
    }
    ?>