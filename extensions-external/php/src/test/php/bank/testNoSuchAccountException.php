<?php

include_once dirname(__FILE__).'/NoSuchAccountException.php';

$e = new NoSuchAccountException("12345");

assert($e->getNumber() == "12345");

assert($e->getMessage() == "Account does not exist: 12345");
