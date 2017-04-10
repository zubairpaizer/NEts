<?php 
	
	include("config.php");
	
	if(isset($_POST)){
	
		$email       = $_POST['email'];
		$password    = sha1($_POST['password']);

		$query = "SELECT * FROM users WHERE email = '{$email}' AND password = '{$password}'";
		$result = mysqli_query($conn, $query);

		if($result){
			$user_data = [];
			while($row = mysqli_fetch_assoc($result)){
				$user_data[] = $row;
			}
			echo json_encode($user_data);
		}else{
			$error = ['error' => 'Error'];
			echo json_encode($error);
		}		
	}
?>
