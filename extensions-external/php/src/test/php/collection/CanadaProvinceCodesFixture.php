<?php

include_once dirname(__FILE__).'/Country.php';

class CanadaProvinceCodesFixture {
	
	function query() {
		return Country::canada()->provinces();
	}
	
}