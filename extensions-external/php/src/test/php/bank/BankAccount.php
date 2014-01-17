<?php

include_once dirname(__FILE__).'/AccountType.php';
include_once dirname(__FILE__).'/Owner.php';

abstract class BankAccount 
{ 
 
  private $type; 
  private $balance; 
  private $number; 
  private $owner; 
 
  public $frozen; 
 
  public function BankAccount(AccountType $accountType, $number, Owner $owner) 
  { 
    $this->number = $number; 
    $this->type = $accountType; 
    $this->owner = $owner; 
	$this->balance = Money::$ZERO;
  } 
 
  public abstract function checkFunds(Money $amount);
 
  public function withdraw(Money $amount, WithdrawType $withdrawType) 
  { 
 
    $limit = $this->type->limitFor($withdrawType); 
    if (!AccountType::isNoLimit($limit) && $amount->strictlyGreaterThan($limit)) 
    { 
      throw new Exception("Limit overpassed"); 
    } 
    $fees = $this->type->feesFor($withdrawType); 
    return $this->withdrawAmmount($amount->plus($fees)); 
  } 
 
  public function withdrawAmmount(Money $amount) 
  { 
    $this->checkNotFrozen(); 
    $this->checkFunds($amount); 
    $this->balance = $this->balance->minus($amount); 
    return $this->balance; 
  } 
 
  private function checkNotFrozen() 
  { 
    if ($this->frozen) 
    { 
      throw new Exception("Acccount frozen!"); 
    } 
  } 
 
  public function getNumber() 
  { 
    return $this->number; 
  } 
 
  public function deposit(Money $amount) 
  { 
    $this->balance = $this->balance->plus($amount); 
    return $this->balance; 
  } 
 
  public function getBalance() 
  { 
    return $this->balance; 
  } 
 
  public function isFrozen() 
  { 
    return $this->frozen; 
  } 
 
  public function freeze() 
  { 
    $this->frozen = true; 
  } 
 
  public function getType() 
  { 
    return $this->type; 
  } 
 
  public function getOwner() 
  { 
    return $this->owner; 
  } 
 
  public function getOwnerName() 
  { 
    return $this->owner->getFullName(); 
  } 
} 
