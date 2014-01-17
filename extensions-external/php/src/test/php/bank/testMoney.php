<?php

include_once dirname(__FILE__).'/Money.php';

function check($o, $val) {
	assert(gettype($o) == 'object');
	assert(get_class($o) == 'Money');
	assert($o->toString() == $val);
}

check(new Money(new BigDecimal(10)), '$10.00');
check(Money::parse('$10'), '$10.00');
check(Money::parse('10,'), '$10.00');
check(Money::parse(' 10'), '$10.00');
check(Money::parse(' 10 '), '$10.00');
check(Money::dollars(11), '$11.00');
check(Money::zero(), '$0.00');

check(Money::$ZERO, '$0.00');

$i = Money::parse(2);
$j = Money::parse(4);
$k = Money::parse(2);

assert(!$i->equals($j));
assert($i->equals($k));

assert($i->times(2)->equals($j));

assert($k->plus($i)->equals($j));
assert($j->minus($i)->equals($k));

assert(true === $j->greaterThan($i));
assert(true === $k->greaterThan($i));

assert(true === $j->strictlyGreaterThan($i));
assert(false === $k->strictlyGreaterThan($i));

assert(true === $i->lowerThan($j));
assert(true === $k->lowerThan($i));

assert(true === $i->strictlyLowerThan($j));
assert(false === $k->strictlyLowerThan($i));

