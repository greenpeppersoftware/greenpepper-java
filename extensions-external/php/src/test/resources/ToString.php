<?php

class NoToString {
	public $a;
	
	public function NoToString($a) {
		$this->a = $a;
	}	
	
}

class WithToString {
	public $a;
	
	public function WithToString($a) {
		$this->a = $a;
	}	
	
	public function toString()
  	{
  		return "ToString Function ".$this->a;
  	}
}
