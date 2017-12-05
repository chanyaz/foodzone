<?php
require_once dirname(__DIR__) . '/lib/CryptLib/CryptLib.php';
require_once dirname(__DIR__) . '/Constants/Constants.php';
class Auth
{
	private $db=NULL;
	public function __construct($db)
	{
		// Init parent contructor
		$this->db=$db;
		// Initiate Database connection
	}

	// checking user
	function check_auth($auth)
	{
		$data=NULL;
		
		$result=mysqli_query($this->db,"CALL procedure_checkauth('".$auth."',".Constants::USER_EXPIRE_TIME.")");
		if($result!=null)
		{
			if(mysqli_num_rows($result) > 0) // it reflect signle record;
			{
				$data = mysqli_fetch_array($result,MYSQLI_ASSOC);
				
			}
			$result->close();
		}
		return $data;
	}


	function getdetails()
	{

	}

	// refresh auth
	function refreshauth($auth,$refresh_token)
	{
		$data=NULL;
		
		$cryptLib = new \CryptLib\CryptLib();
		$new_auth=$cryptLib->getRandomToken(32);
		$new_refresh_token=$cryptLib->getRandomToken(16);	
		$sql = "CALL procedure_refreshauth('".$auth."','".$refresh_token."','".$new_auth."','".$new_refresh_token."')";
		$result=mysqli_query($this->db,$sql);
		if($result!=null)
		{
		if(mysqli_num_rows($result) > 0) // it reflect signle record;
		{
			$data = mysqli_fetch_array($result,MYSQLI_ASSOC);
			
		}
		$result->close();
		}
		return $data;
		
		
	}


	


}

?>