<?php

class Calculator {
	private $x;
	
	private $y;
	
	function setX($x) {
		$this->x = $x;
	}
	
	function setY($y) {
		$this->y = $y;
	}
	
	function Sum() {
		return $this->x + $this->y;
	}
	
	function Product() {
		return $this->x * $this->y;
	}
	
	function Quotient() {
		if ($this->y == 0) {
			throw new Exception("Divide by zero");
		}
		return $this->x / $this->y;
	}
}

