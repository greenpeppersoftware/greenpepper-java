<?php

class ImplicitEquals {
	public $a;
	
	public function ImplicitEquals($a) {
		$this->a = $a;
	}	
}

class ExplicitEquals {
	public $a;
	
	public function ExplicitEquals($a) {
		$this->a = $a;
	}	
	
	public function equals($other)
  	{
  		if (get_class($other) == "ExplicitEquals")
    	{
     		return $this->a == $other->a;
    	}
    	else
    	{
      		return false;
    	}
  	}
}
