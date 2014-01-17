<?php

include_once 'CanadaProvincesCodesFixture.php';

$o = new CanadaProvinceCodesFixture();
$z = $o->query();

var_dump($z, $z[0]);

echo serialize(Country::canada())."\n";