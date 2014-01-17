<?php

include_once dirname(__FILE__).'/CheckingAccount.php';

$owner = new Owner("First", "Last");

$account = new CheckingAccount("123456", $owner);

assert($account->getNumber() == "123456");

assert($account->getOwner() == $owner);
assert($account->getOwnerName() == "First Last");

assert($account->getBalance()->toString() == '$0.00');

$account->deposit(Money::parse("200"));

assert($account->getBalance()->toString() == '$200.00');

$account->withdrawAmmount(Money::parse("10"));

assert($account->getBalance()->toString() == '$190.00');

$account->withdraw(Money::parse("10"), WithdrawType::$INTERAC);

assert($account->getBalance()->toString() == '$177.50');
