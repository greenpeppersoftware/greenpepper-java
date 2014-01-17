<?php

class WithdrawType 
{ 
  public static $ATM;
  public static $INTERAC;
  public static $PERSONAL_CHECK;
 
  private $description; 
  private $id;
  
  private static $counter = 0;
 
  private function WithdrawType($description) 
  { 
    $this->description = $description; 
	$this->id = WithdrawType::$counter;
	WithdrawType::$counter ++;
  } 
  
  public function toString() 
  { 
    return $this->description; 
  } 
  
  public function getId() {
	return $this->id;
  }
  
  public static function init() {  
	WithdrawType::$ATM = new WithdrawType("ATM");
	WithdrawType::$INTERAC = new WithdrawType("Interact");
	WithdrawType::$PERSONAL_CHECK = new WithdrawType("Personal Check"); 
  }
} 

WithdrawType::init();
