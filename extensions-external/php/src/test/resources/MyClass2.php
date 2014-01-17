<?php

class SubClass {

	public $z;
	
	public function SubClass($z) {
		$this->z = strlen($z);
	}
	
	public static function parse($s) {
		return new SubClass($s);
	}
}

class MyClass2 {
	public $x;
	public $y;
	public $z;
	
	public function getX(Exception $e, $s) {
		return $this->x;
	}
	
	public function getY(SubClass $s) {
		return $s->z;
	}
	
	private function getZ() {
		return $this->z;
	}
	
	public function echoo($s1, $s2) {
		return $s1.$s2;
	}
	
}