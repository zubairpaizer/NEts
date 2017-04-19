<?php 
	
	include("config.php");

	
	if(isset($_POST)){
	
		$first_name  = $_POST['first_name'];
		$last_name   = $_POST['last_name'];
		$email       = $_POST['email'];
		$phone_numer = $_POST['phone'];
		$cnic		 = $_POST['cnic'];
		$password    = sha1($_POST['password']);

		$query = "INSERT INTO owners VALUES (NULL, '{$first_name}', '{$last_name}', '{$email}','{$phone_numer}', 'path', '{$cnic}', '{$password}',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

		$result = mysqli_query($conn, $query);

		//var_dump($query);

		if($result){
			$message = ['message' => 'Successfully Signup'];
			echo json_encode($message);
		}else{
			$error = ['error' => 'Error'];
			//echo json_encode(var_dump($_POST));
		}		
	}
?>