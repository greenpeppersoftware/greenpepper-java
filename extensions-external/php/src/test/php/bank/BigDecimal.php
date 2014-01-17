<?php

class BigDecimal {
	
	private $val;
	
	public function BigDecimal($val) {
		$this->val = $val;
	}
	
	public function multiply($multiplier) {
		return new BigDecimal($this->val * $multiplier);
	}
	
	public function compareTo($obj) {
		if ($obj->val < $this->val) {
			return 1;
		}
		if ($obj->val == $this->val) {
			return 0;
		}
		return -1;
	}
	
	public function toString() {
		return sprintf("%0.2f", $this->val);
	}
	
	public function add($k) {
		return new BigDecimal($this->val + $k->val);
	}
	
	public function substract($k) {
		return new BigDecimal($this->val - $k->val);
	}
}