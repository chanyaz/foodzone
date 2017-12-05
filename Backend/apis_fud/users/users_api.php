<?php
require_once("Rest.inc.php");
require_once dirname(__DIR__) . '/lib/CryptLib/CryptLib.php';
require_once dirname(__DIR__) . '/Constants/Constants.php';
require_once dirname(__DIR__) . '/Auth/Auth.php';
		
class USERS_API extends REST
{

	
		public $data = "";
		
		public $db = NULL;
		

		
	
		public function __construct()
		{
			
			parent::__construct();				// Init parent contructor
			$this->dbConnect();	
			// Initiate Database connection
		}



		// db connect by class object create
		private function dbConnect()
		{	
			$this->db = mysqli_connect(Constants::DB_SERVER,Constants::DB_USER,Constants::DB_PASSWORD,Constants::DB);	
		
		}


		// process api
		public function processApi()
		{

			if(!empty($_REQUEST['request']))
			{
			
				$path=explode("/",strtolower($_REQUEST['request']));
				if(is_array($path))
				{
				    if(count($path)>=2)
					{

						if(strcmp("v14",$path[0])==0)
						{
							if((int)method_exists($this,$path[1]) > 0)
							{
							//if(!empty($func[]))
							$func=$path[1];
							$this->$func();
							}
							else
							{
								$this->showError();
							}
						}
						else
						{
							$this->showError();
						}
					}
					else
					{
					     $this->showError();
					}
				}
				else
				{
	        		$this->showError();
				}

			}
			else
			{
				$this->showError();
			}

			$this->dbClose();
		
		}


		// register 
		private function register()
		{
			 	$variables=json_decode(file_get_contents("php://input"),true);
				if(!empty($variables) && count($variables)==2 && array_key_exists('phone_number',$variables) && array_key_exists('m', $variables))
				{
						
						if(strlen($variables["phone_number"])==10)
						{

								$result=mysqli_query($this->db,"CALL procedure_users('".$variables["phone_number"]."','".$variables["country_prefix"]."')");
								if(mysqli_num_rows($result) > 0) // it reflect signle record;
								{
								$data = mysqli_fetch_array($result,MYSQLI_ASSOC);
								$result->close();
								header('Content-Type: application/json');
								$success["status"]=true;
								if(!array_key_exists('details_id',$data))
								{
									$data["register"]=false;
									$success["message"]="user is not registered";
								}
								else
								{
									$data["register"]=true;
									$success["message"]="user registered already";
								}
								$success["data"]=$data;
								$this->response($this->json($success), 200);
								}
								else
								{
								$fail["status"]=false;
								$fail["message"]="invalid operation";
								$this->response($this->json($fail),200);
								}
						}
						else
						{

						        $fail["status"]=false;
								$fail["message"]="invalid phone number";
								$this->response($this->json($fail),200);

						}

						
				}
				else
				{
						$this->showError();
				}
		}

		// user details 
		private function userdetails()
		{
				$variables=json_decode(file_get_contents("php://input"),true);
				if(!empty($variables) && count($variables)==4 && array_key_exists('user_id',$variables) && array_key_exists('email', $variables) && array_key_exists('first_name',$variables) && array_key_exists('last_name',$variables))
				{

								$result=mysqli_query($this->db,"CALL procedure_user_details('".$variables["user_id"]."','".$variables["first_name"]."','".$variables["last_name"]."','".$variables["email"]."')");
								if(mysqli_num_rows($result) > 0) // it reflect signle record;
								{
								$data = mysqli_fetch_array($result,MYSQLI_ASSOC);
								$result->close();
								header('Content-Type: application/json');
								$success["status"]=true;
								$success["message"]="valid operation";
								$success["data"]=$data;
								$this->response($this->json($success), 200);
								}
								else
								{
								$fail["status"]=false;
								$fail["message"]="invalid operation";
								$this->response($this->json($fail),200);
								}
					
						
				}
				else
				{
						$this->showError();
				}
		}

		//user fcms
		private function fcmtoken()
		{
				$variables=json_decode(file_get_contents("php://input"),true);
				if(!empty($variables) && count($variables)==5 && array_key_exists('auth',$variables) && array_key_exists('device_id', $variables) && array_key_exists('fcm_token',$variables) && array_key_exists('platform',$variables) && array_key_exists('version',$variables))
				{
								$data=$this->check_auth();
								$sql="CALL procedure_fcms('".$data["user_id"]."','".$variables["device_id"]."','".$variables["fcm_token"]."','".$variables["platform"]."','".$variables["version"]."')";
								echo $sql;
								$result=mysqli_query($this->db,$sql);
								if(mysqli_num_rows($result) > 0) // it reflect signle record;
								{
								$data = mysqli_fetch_array($result,MYSQLI_ASSOC);
								$result->close();
								header('Content-Type: application/json');
								$success["status"]=true;
								$success["message"]="valid operation";
								$success["data"]=$data;
								$this->response($this->json($success), 200);
								}
								else
								{
								$fail["status"]=false;
								$fail["message"]="invalid operation";
								$this->response($this->json($fail),200);
								}
					
						
				}
				else
				{
						$this->showError();
				}	
			
		}

		//user auth check
		private function checkauth()
		{
			
			$variables=json_decode(file_get_contents("php://input"),true);
			if(!empty($variables) && array_key_exists('auth', $variables))
			{

				$auth = new Auth($this->db);
				$data=$auth->check_auth($variables["auth"]);
				if($data!=NULL)
				{
						if(array_key_exists('user_id', $data) && $data["user_id"]!=NULL)
						{
										
										if($data["flag"]==1)
										{
											return $data;
										}
										else
										{

											$fail["status"]=false;
											$fail["expired"]=true;
											$fail["unauthorized"]=false;
											$fail["message"]="user auth expired";
											$this->response($this->json($fail),200);

										}
						}
						else
						{
							$fail["status"]=false;
							$fail["expired"]=false;
							$fail["unauthorized"]=true;
							$fail["message"]="unauthorized user";
							$this->response($this->json($fail),200);
						}
				}
				else
				{
					$this->showError();
				}
			
			}
			else
			{
				$this->showError();
			}
		}

		//refresh auth
		private function refreshauth()
		{
			$variables=json_decode(file_get_contents("php://input"),true);
			$auth = new Auth($this->db);
			if(!empty($variables) && array_key_exists('auth', $variables) && array_key_exists('refresh_token', $variables))
			{
				$data=$auth->refreshauth($variables["auth"],$variables["refresh_token"]);
				if($data!=null && array_key_exists('auth', $data))
				{
					$fail["status"]=true;
					$fail["message"]="user auth refreshed";
					$fail["data"]=$data;
					$this->response($this->json($fail),200);
				}
				else
				{
					$fail["status"]=false;
					$fail["expired"]=false;
					$fail["unauthorized"]=true;
					$fail["message"]="unauthorized user";
					$this->response($this->json($fail),200);
				}
			}
			else
			{
				$this->showError();
			}

		}

		private function verifydetails()
		{

		}

		
		//facebook account kit
		private function verifyauthorizationcode()
		{
			
				$variables=json_decode(file_get_contents("php://input"),true);
				if(!empty($variables) && count($variables)==1 && array_key_exists('authorization_code',$variables))
				{
						
						$authorization_code = $variables["authorization_code"];
						$access_token='AA|'.Constants::FACEBOOK_APP_ID.'|'.Constants::FACEBOOK_APP_SECRET;
						$url = 'https://graph.accountkit.com/v1.2/access_token?grant_type=authorization_code&code='.$authorization_code.'&access_token='.$access_token;
						
						$ch = curl_init($url);
						curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
						$response =  curl_exec($ch);
						//$response = '{"id":"1464775440244091","access_token":"EMAWf6s1JEVskQwVtCzijZBl3WAavWvdK8OKwNjMEOmRARPLfTNkZAMHS1wT3kSOfDeMb20twAGEdnOFZCbmtR7ZBmwu5z5oq60bEZABUmdN8uaqLBt9R7sbNZC462Cldpt4nSwZAn9ZBRaE0Mw23Rs7CInMJKAUNZBgJsZD","token_refresh_interval_sec":2592000}';
						curl_close($ch);
						$content = json_decode($response,true);

						
						
						
						if(array_key_exists('access_token',$content))
						{
								$url = 'https://graph.accountkit.com/v1.2/me/?access_token='.$content["access_token"].'&appsecret_proof='.hash_hmac('sha256', $content["access_token"], Constants::FACEBOOK_APP_SECRET);;
								$ch = curl_init($url);
								curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
								$response1 = curl_exec($ch);
								curl_close($ch);
								$content = json_decode($response1,true);
								if(array_key_exists('id',$content))
								{
								
											$result=mysqli_query($this->db,"CALL procedure_users('".$content["phone"]["national_number"]."','".$content["phone"]["country_prefix"]."')");
											if(mysqli_num_rows($result) > 0) // it reflect signle record;
											{
												$data = mysqli_fetch_array($result,MYSQLI_ASSOC);
												$result->close();
												header('Content-Type: application/json');
												$success["status"]=true;
												if(!array_key_exists('details_id',$data))
												{
												$data["profile"]=false;
												$success["message"]="user other details not exist";
												}
												else
												{
												$data["profile"]=true;
												$success["message"]="user other details exist";
												}
												$success["data"]=$data;
												$this->response($this->json($success), 200);
											}
											else
											{
												$fail["status"]=false;							
												$fail["message"]="unauthorized token";
												$this->response($this->json($fail),200);
											}
								}
								else
								{
								$fail["status"]=false;							
								$fail["message"]="unauthorized token";
								$this->response($this->json($fail),200);
								}
								//$this->response($this->json($response1),200);
						}
						else
						{
							
							$fail["status"]=false;							
							$fail["message"]="unauthorized token";
							$this->response($this->json($fail),200);
							
						}

						
						
				}
				else
				{
						$this->showError();
				}
				
		}

		


		// db close
		private function dbClose()
		{
			mysqli_close($this->db);
		
		}

		// throw 404 error
		private function showError()
		{
			$this->dbClose();
			$this->response('',404);
		}

	


		// data to json
		private function json($data)
		{
			$this->dbClose();
			if(is_array($data))
			{
				return json_encode($data);
			}
		}







}
$api = new USERS_API();
$api->processApi();



?>