<?php

include_once dirname(__FILE__).'/BankAccount.php';

class SavingsAccount extends BankAccount 
{ 
  public function SavingsAccount($number, Owner $owner) 
  { 
    parent::BankAccount(AccountType::$SAVINGS, $number, $owner); 
  } 
 
  public function checkFunds(Money $amount) 
  { 
    if ($this->getBalance()->lowerThan($amount)) 
    { 
      throw new Exception("Not enougth money !"); 
    } 
  } 
} 