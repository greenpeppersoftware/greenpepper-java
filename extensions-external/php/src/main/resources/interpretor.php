<?php

/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 *
 * IMPORTANT NOTE :
 * Kindly contributed by Bertrand Paquet from Octo Technology (http://www.octo.com)
 */

//$fp = fopen("php://stdout", "w");

$port = $argv[1];

$fp = stream_socket_client("tcp://127.0.0.1:$port", $errrno, $errstr, 10);

fwrite($fp, base64_encode("Starting interpretor. Port ".$port.". Current directory = ".getcwd())."\n");

$phpsud_on_error = false;
$phpsud_last_error = "";

$phpsud_index = 0;
$phpsud_list = array();

$phpsud_static_header = "PHPOBJ_STATIC_";

function phpsud_getStatic($s) {
	global $phpsud_static_header;
	$name = substr($s, strlen($phpsud_static_header));
	$t = split("_", $name);
	if (count($t) != 2) {
		return null;
	}
	$cmd = '$ret = '.$t[0].'::$'.$t[1].";";
	eval($cmd);
	return $ret;
}

function phpsud_getStaticVar($className) {
	$a = array();
	$class = new ReflectionClass($className);
	foreach($class->getProperties() as $p) {
		if ($p->isPublic() && $p->isStatic()) {
			array_push($a, $p->getName());
		}
	}
	return $a;
}

function phpsud_getMethodsInClass($className) {
	$a = array();
	$class = new ReflectionClass($className);
	array_push($a, $class->getName());
	foreach($class->getMethods() as $m) {
		if ($m->isPublic()) {
			array_push($a, $m->getName());
		}
	}
	return $a;
}

function phpsud_getParamsInMethod($className, $methodName) {
	$a = array();
	$class = new ReflectionClass($className);
	foreach($class->getMethods() as $m) {
		if ($m->getName() == $methodName) {
			foreach($m->getParameters() as $p) {
				array_push($a, $p->getClass() == null ? "null" : $p->getClass()->getName());
			}
		}
	}
	return $a;
}

function phpsud_saveObject($obj) {
	global $phpsud_list, $phpsud_index;
	
	$name = "PHPSUD_OBJ_".$phpsud_index;
	$phpsud_list["$name"] = $obj;
	$phpsud_index ++;
	return $name;
}

function phpsud_getObject($id) {
	global $phpsud_list, $phpsud_static_header;
	
	if (substr($id, 0, strlen($phpsud_static_header)) == $phpsud_static_header) {
		return phpsud_getStatic($id);
	}
	return $phpsud_list[$id];
}

function phpsud_getArray($array) {
	if (!is_array($array)) {
		return;
	}
	$a = array();
	foreach($array as $e) {
		array_push($a, phpsud_saveObject($e));
	}
	return $a;
}

function phpsud_error_handler($errno, $errstr, $errfile, $errline)
{
	global $phpsud_on_error, $phpsud_last_error;
	switch($errno){
	case E_USER_ERROR:
		$phpsud_on_error = true;
		$phpsud_last_error = $errstr;
		return true;
	default:
		return false;
	}
}

set_error_handler("phpsud_error_handler");

while($phpsud_line = base64_decode(fgets($fp))) {
	$phpsud_header = substr($phpsud_line, 0, 1);
	$phpsud_cmd = substr($phpsud_line, 1);
	$phpsud_cmd_real = "";
	if ($phpsud_header == "G") {
		$phpsud_cmd_real = '$phpsud_res = '.$phpsud_cmd;
	}
	else if ($phpsud_header == "R") {
		$phpsud_cmd_real = $phpsud_cmd;
		$phpsud_res = "OK";
	}
	else {
		fwrite($fp, base64_encode("__SYNTAX_ERROR__")."\n");
	}
	if ($phpsud_cmd_real != "") {
		try {
			$phpsud_eval_res = eval($phpsud_cmd_real);
		}
		catch (Exception $e) {
			$phpsud_eval_res = 1;
			$phpsud_on_error = true;
			$phpsud_last_error = "$e";
		}
		if ($phpsud_eval_res === false) {
			fwrite($fp, base64_encode("__SYNTAX_ERROR__")."\n");
		}
		elseif ($phpsud_on_error) {
			fwrite($fp, base64_encode("__EXEC_ERROR__ cmd='".trim($phpsud_cmd)."', msg='".$phpsud_last_error."'")."\n");
			$phpsud_on_error = false;
		}
		else {
			fwrite($fp, base64_encode($phpsud_res)."\n");
		}
	}
}
