<?php

class ParseClass {
	public $a;
	
	public function ParseClass($a) {
		$this->a = $a;
	}	
	
	public static function parse($text) {
		return new ParseClass("ParseClass $text");
	}
	
	public function getA() {
		return $this->a;
	}
}
