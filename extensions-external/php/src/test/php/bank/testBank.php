<?php

include_once dirname(__FILE__).'/Bank.php';

$b = new Bank();

assert(count($b->getAccounts()) == 0);

$owner = new Owner("First", "Last");

$account = $b->openCheckingAccount("123", $owner);
assert(count($b->getAccounts()) == 1);

assert($b->getAccount("123")->getBalance()->toString() == '$0.00');

$b->deposit(Money::parse("200"), "123");

assert($b->getAccount("123")->getBalance()->toString() == '$200.00');

$account2 = $b->openCheckingAccount("321", $owner);
assert(count($b->getAccounts()) == 2);

assert($b->getAccount("321")->getBalance()->toString() == '$0.00');

$b->transfer("123", "321", Money::parse("100"));

assert($b->getAccount("123")->getBalance()->toString() == '$100.00');
assert($b->getAccount("321")->getBalance()->toString() == '$100.00');