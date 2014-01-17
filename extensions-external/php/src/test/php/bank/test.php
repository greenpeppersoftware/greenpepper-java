<?php

$dir = dirname(__FILE__);

foreach(scandir($dir) as $d) {
	if (substr($d, 0, 4) == "test" && $d != basename(__FILE__)) {
		print "Running $d\n";
		include_once $dir."/".$d;
	}
}