<?php 
	
	include("config.php");
	
	if(isset($_GET)){

		$query = "SELECT 
					first_name, 
					last_name, 
					cnic, 
					email,
					profile_image
					phone_number, 
					cnic 
					FROM users";
		
		$result = mysqli_query($conn, $query);

		if($result){
			$users = [];
			while ($row = mysqli_fetch_assoc($result)) {
				$users[] = $row; 
			}
			echo json_encode($users);
		}else{
			$error = ['error' => 'Error'];
			echo json_encode($error);
		}		
	}
?>
