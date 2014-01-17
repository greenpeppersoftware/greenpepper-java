<?php

include_once dirname(__FILE__).'/Province.php';

class Country {
	private $name;
	
	private $provinces = array();
	
	function Country($name) {
		$this->name = $name;
	}
	
	function getName() {
		return $this->name;
	}
	
	function setName($name) {
		$this->name = $name;
	}
	
	function provinces() {
		return $this->provinces;
	}
	
	static function canada() {
		$canada = new Country("CANADA");
		array_push($canada->provinces, new Province("ALBERTA", "AB"));
		array_push($canada->provinces, new Province("BRITISH COLUMBIA", "BC"));
		array_push($canada->provinces, new Province("MANITOBA", "MB"));
		array_push($canada->provinces, new Province("NEW BRUNSWICK", "NB"));
		array_push($canada->provinces, new Province("NEWFOUNDLAND and LABRADOR", "NL"));
		array_push($canada->provinces, new Province("NOVA SCOTIA", "NS"));
		array_push($canada->provinces, new Province("NUNAVUT", "NU"));
		array_push($canada->provinces, new Province("ONTARIO", "ON"));
		array_push($canada->provinces, new Province("PRINCE EDWARD ISLAND", "PE"));
		array_push($canada->provinces, new Province("QUEBEC", "QC"));
		array_push($canada->provinces, new Province("SASKATCHEWAN", "SK"));
		array_push($canada->provinces, new Province("YUKON", "YT"));
		return $canada;
	}
}
