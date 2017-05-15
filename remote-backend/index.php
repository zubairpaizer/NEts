<?php

require('../vendor/autoload.php');
require('./Config.php');

/************************************************************************************************ 
*                                       users/employees routes                                   *  
*                                                                                                *
*************************************************************************************************/

/************************************ all users/employees ***************************************/

$app->get('/employees', function() use($app, $conn) {
     $user = ORM::for_table('users')
                  ->select_many('id','first_name','last_name','email','phone_number','profile_image','password','cnic','updated_at','created_at')
                  ->find_array();
    if(!$user){
        $message = array('message' => 'record not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($user), 200);
    }
});

/********************************** users/employees with id ***************************************/

$app->get('/employee/{id}', function($id) use($app, $conn) {
    $user = ORM::for_table('users')
                ->where('id',$id)
                ->select_many('id','first_name','last_name','email','phone_number','profile_image','cnic','updated_at','created_at')
                ->find_array();
    if(!$user){
        $message = array('message' => 'record not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($user), 200);
    }
});


/********************************** patch users/employees with id ************************************/
// this routes updates all recored excpet password and email address.

$app->patch('/employee/{id}', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
    
    $user = ORM::for_table('users')->find_one($id);

    $user->set(array(
        'first_name'     => $request->get('first_name'),
        'last_name'      => $request->get('last_name'),
        'phone_number'   => $request->get('phone_number'),
        'profile_image'  => $request->get('profile_image'),
        'cnic'           => $request->get('cnic'),
        'updated_at'     => 'NOW()',
    ));


    $user->save();

    if(!$user){
        $message = array('message' => 'patched failed', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        $message = array('message' => 'record patched', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }

});



/********************************** delete users/employees with id ************************************/

$app->delete('/employee/{id}', function($id) use($app, $conn) {
    $user = ORM::for_table('users')->find_one($id);
    $user->delete();
    if(!$user){
        $message = array('message' => 'delete failed', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        $message = array('message' => 'record delete', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }
});

/************************************************************************************************ 
*                                      owner routes                                              *  
*                                                                                                *
*************************************************************************************************/

/************************************* all owners ***********************************************/
$app->get('/owners', function() use($app, $conn) {
     $owners = ORM::for_table('owners')
                  ->select_many('id','first_name','last_name','email','phone_number','profile_image','cnic','updated_at','created_at')
                  ->find_array();
    if(!$owners){
        $message = array('message' => 'record not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($owners), 200);
    }
});

/****************************************** owner with id ****************************************/
$app->get('/owner/{id}', function($id) use($app, $conn) {
   $owner = ORM::for_table('owners')
                ->where('id',$id)
                ->select_many('id','first_name','last_name','email','phone_number','profile_image','cnic','updated_at','created_at')
                ->find_array();
    if(!$owner){
        $message = array('message' => 'record not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($owner), 200);
    }
});

/********************************** patch owner with id ************************************/
// this routes updates all recored excpet password and email address.

$app->patch('/owner/{id}', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
    
    $owner = ORM::for_table('users')->find_one($id);

    $owner->set(array(
        'first_name'     => $request->get('first_name'),
        'last_name'      => $request->get('last_name'),
        'phone_number'   => $request->get('phone_number'),
        'profile_image'  => $request->get('profile_image'),
        'cnic'           => $request->get('cnic'),
        'updated_at'     => 'NOW()',
    ));


    $owner->save();

    if(!$owner){
        $message = array('message' => 'patched failed', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        $message = array('message' => 'record patched', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }

});



/********************************** delete owner with id ************************************/

$app->delete('/owner/{id}', function($id) use($app, $conn) {
    $owner = ORM::for_table('owners')->find_one($id);
    $owner->delete();
    if(!$owner){
        $message = array('message' => 'delete failed', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        $message = array('message' => 'record delete', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }
});

/************************************************************************************************ 
*                                      locations routes                                          *  
*                                                                                                *
*************************************************************************************************/

/************************************* all locations ********************************************/
$app->get('/locations', function() use($app, $conn) {
  $locations = ORM::for_table('locations')
                                    ->join('users','locations.user_id = users.id')
                                    ->select_many(array('location_id' => 'locations.id'),array('user_id' => 'users.id'),'first_name','last_name','email','phone_number','profile_image','cnic','langitude','latitude')
                                    ->find_array();
    if(!$locations){
        $message = array('message' => 'location not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($locations), 200);
    }
});
/************************************ locations with id *******************************************/
$app->get('/location/{id}', function($id) use($app, $conn) {
  $location = ORM::for_table('locations')
            ->where('locations.id',$id)
            ->join('users','locations.user_id = users.id')
            ->select_many(array('location_id' => 'locations.id'),array('user_id' => 'users.id'),'first_name','last_name','email','phone_number','profile_image','cnic','langitude','latitude')
            ->find_array();
    if(!$location){
        $message = array('message' => 'location not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($location), 200);
    }
});

/************************************ locations with user_id ****************************************/

$app->get('/locations/user/{user_id}', function($user_id) use($app, $conn) {
    $locations = ORM::for_table('locations')
               ->where('locations.user_id',$user_id)
               ->join('users','locations.user_id = users.id')
               ->select_many(array('location_id' => 'locations.id'),array('user_id' => 'users.id'),'first_name','last_name','email','phone_number','profile_image','cnic','langitude','latitude','time_at')
               ->find_array();
    if(!$locations){
        $message = array('message' => 'location not found', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($locations), 200);
    }
});

/************************************ save locations with user_id ****************************************/

$app->post('/location/user/save', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
    $location = ORM::for_table('locations')->create();
    // spell should be change of longitude
    $location->langitude = $request->get('langitude');
    $location->latitude = $request->get('latitude');
    $location->user_id = $request->get('user_id');
    $location->time_at = "NOW()";
    $location->save();

    if($location){
        $message = array('message' => 'location has saved', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }else{
        $message = array('message' => 'some thing went wrong during location saving', 'code' => '500');
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 500);
    }
});

/********************************** delete location with id ************************************/

$app->delete('/location/{id}', function($id) use($app, $conn) {
    $location = ORM::for_table('locations')->find_one($id);
    $location->delete();
    if(!$location){
        $message = array('message' => 'delete failed', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        $message = array('message' => 'record delete', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }
});


/********************************** delete location with user_id ************************************/

$app->delete('/locations/{user_id}', function($user_id) use($app, $conn) {

    $locations = ORM::for_table('locations')->find_many($id);
    $locations->delete_many();

    if(!$locations){
        $message = array('message' => 'delete failed', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        $message = array('message' => 'record delete', 'code' => 200);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }

});

/************************************************************************************************ 
*                                      login routes                                              *  
*                                                                                                *
*************************************************************************************************/


/************************************ user/employee login ****************************************/
$app->post('/login/user', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
    
    $email = $request->get('email');
    $password = $request->get('password');

    $user = ORM::for_table('users')
                    ->where('email',$email)
                    ->where('password',sha1($password))
                    ->find_array();
   
    if(!$user){
        $message = array('message' => 'invalid email/password', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($user), 200);
    }
});

/******************************************* owner login *****************************************/
$app->post('/login/owner', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
    
    $email = $request->get('email');
    $password = $request->get('password');

    $owner = ORM::for_table('owners')
                    ->where('email',$email)
                    ->where('password',sha1($password))
                    ->find_array();
   
    if(!$owner){
        $message = array('message' => 'invalid email/password', 'code' => 404);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 404);
    }else{
        return new \Symfony\Component\HttpFoundation\Response(json_encode($owner), 200);
    }

});

/************************************************************************************************ 
*                                      Signup routes                                             *  
*                                                                                                *
*************************************************************************************************/

/************************************ user/employee singup ***************************************/
$app->post('/signup/user', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
      $user = ORM::for_table('users')->create();
      $user->first_name = $request->get('first_name');
      $user->last_name = $request->get('last_name');
      $user->email = $request->get('email');
      $user->phone_number = $request->get('phone');
      $user->profile_image = 'path';
      $user->cnic = $request->get('cnic');
      $user->password = sha1($request->get('password'));
      $user->updated_at = "NOW()";
      $user->created_at = "NOW()";
      $user->save();
        if($user){
            $message = array('message' => 'user added', 'code' => 200);
            return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
        }else{
            $message = array('message' => 'some thing went wrong during user singup', 'code' => 500);
            return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 500);
        }
});

/******************************************* owner singup ********************************************/
$app->post('/signup/owner', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn) {
    $owner = ORM::for_table('owners')->create();
    $owner->first_name = $request->get('first_name');
    $owner->last_name = $request->get('last_name');
    $owner->email = $request->get('email');
    $owner->phone_number = $request->get('phone');
    $owner->profile_image = 'path';
    $owner->cnic = $request->get('cnic');
    $owner->password = sha1($request->get('password'));
    $owner->updated_at = "NOW()";
    $owner->created_at = "NOW()";
    $owner->save();
    if($owner){
        $message = array('message' => 'owner added', 'code' => '200');
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);
    }else{
        $message = array('message' => 'some thing went wrong during owner singup', 'code' => 500);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 500);
    }
});


/************************************************************************************************ 
*                                      password rest                                             *  
*                                                                                                *
*************************************************************************************************/

/************************************ user/employee password reset ***************************************/

//$app->post('/passwordrest/user', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn){
     
/*     if ($email){
        $salt = "998#2183B631%38002D!801600D*7E3CC13";
        
        $password = hash('sha512', $salt.$email);
 
        $pwrurl = "www.domain.com/reset_password.php?q=".$password;
         
        $mailbody = "bala bala bala $pwrurl";

        mail($email, "www.domain.com - Password Reset", $mailbody);


        $message = array('message' => 'email has sent please check your email', 'code' => 200);
        
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);

    }
    else
        $message = array('message' => 'some thing went wrong please retry after some 5 minutes', 'code' => 500);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 500);
    }*/

//})



/************************************ owner password reset ***************************************/

//$app->post('/passwordrest/owner', function(\Symfony\Component\HttpFoundation\Request $request) use($app, $conn){
     
/*     if ($email){

        $salt = "998#2183B631%38002D!801600D*7E3CC13";
        
        $password = hash('sha512', $salt.$email);
 
        $pwrurl = "www.domain.com/reset_password.php?q=".$password;
         
        $mailbody = "bala bala bala $pwrurl";

        mail($email, "www.domain.com - Password Reset", $mailbody);


        $message = array('message' => 'email has sent please check your email', 'code' => 200);
        
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 200);

    }
    else

        $message = array('message' => 'some thing went wrong please retry after some 5 minutes', 'code' => 500);
        return new \Symfony\Component\HttpFoundation\Response(json_encode($message), 500);

    }*/
//})



$app->run();
