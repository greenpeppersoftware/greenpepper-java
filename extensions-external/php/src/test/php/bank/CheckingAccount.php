<?php

include_once dirname(__FILE__).'/BankAccount.php';

class CheckingAccount extends BankAccount 
{ 
 
  private $maxCredit;
 
  public function CheckingAccount($number, $owner) 
  { 
    parent::BankAccount(AccountType::$CHECKING, $number, $owner); 
	$this->maxCredit = Money::zero();
  } 
 
  public function checkFunds(Money $amount) 
  { 
    if ($this->getBalance()->plus($this->maxCredit)->lowerThan($amount)) 
    { 
      throw new Exception("Not enougth credit !"); 
    } 
  } 
 
  public function setCreditLine(Money $credit) 
  { 
    $this->maxCredit = $credit; 
  } 
 
  public function limitFor(WithdrawType $type) 
  { 
    return AccountType::$CHECKING->limitFor($type); 
  } 
} 