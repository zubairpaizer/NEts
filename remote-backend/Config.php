<?php 

	$app = new Silex\Application();
	$app['debug'] = true;
	
	// get heroku env link
	$dbopts = parse_url(getenv('DATABASE_URL'));

	// config
	$host = $dbopts["host"];
	$user = $dbopts["user"];
	$port = $dbopts["port"];
	$pass = $dbopts["pass"];
	$dbname = ltrim($dbopts["path"],'/');

    ORM::configure(array(
        'connection_string' => "pgsql:host={$host};port={$port};dbname={$dbname}",
        'username' => $user,
        'password' => $pass
    ));

    ORM::configure('return_result_sets', true); // returns result sets

