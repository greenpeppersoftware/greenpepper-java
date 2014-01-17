<?php

class StaticClass {
	public static $A;
	public static $B;
	
	private $val;
	
	public function getVal() {
		return $this->val;
	}
	
	public function StaticClass($val) {
		$this->val = $val;
	}
	
	public static function init() {
		StaticClass::$A = new StaticClass("a");
		StaticClass::$B = new StaticClass("b");
	}
	
}

StaticClass::init();