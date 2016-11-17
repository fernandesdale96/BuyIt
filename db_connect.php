<?php
 
/**
 * A class file to connect to database
 */
class DB_CONNECT {
    
    private $connect;
    
    // constructor
    function __construct() {
        // connecting to database
        $this->connect = $this->connection();
    }
 
    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }
 
    /**
     * Function to connect with database
     */
    function connection() {
        // import database connection variables
        require_once __DIR__ . '/db_config.php';
 
        // Connecting to mysql database
        $con = @mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD);
 
        // Selecing database
        $db = mysql_select_db(DB_DATABASE);
 
        // returing connection cursor
        return $con;
    }
 
    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        mysql_close();
    }

    public function getDB(){
        return $this->connect;
    }
 
}
 
?>