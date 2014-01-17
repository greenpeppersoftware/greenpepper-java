<?php

include_once 'calculator.php';

$obj_0 = new Calculator();

$obj_0->setX(1);
$obj_0->setY(2);

$res = $obj_0->Sum();
echo json_encode($res)."\r\n";
$res = $obj_0->Product();
echo json_encode($res)."\r\n";
$res = $obj_0->Quotient();
echo json_encode($res)."\r\n";
