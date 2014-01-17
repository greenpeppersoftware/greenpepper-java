<?php

include_once dirname(__FILE__).'/BigDecimal.php';

$i = new BigDecimal(0);
$j = new BigDecimal(0);

$k = new BigDecimal(2);

assert(gettype($i->toString()) == 'string');
assert($i->toString() == "0");

assert($i->compareTo($j) == 0);

assert($i->compareTo($k) == -1);

assert($k->compareTo($i) == 1);

assert(get_class($k->multiply(2)) == "BigDecimal");

assert($k->multiply(2)->toString() == "4");

assert($k->add(new BigDecimal(1))->toString() == "3");
assert($k->substract(new BigDecimal(1))->toString() == "1");