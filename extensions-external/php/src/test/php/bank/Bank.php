<?php

include_once dirname(__FILE__).'/SavingAccount.php';
include_once dirname(__FILE__).'/CheckingAccount.php';
include_once dirname(__FILE__).'/NoSuchAccountException.php';

class Bank 
{ 
 
  private $accounts = array(); 
 
  public function Bank() 
  { 
  } 
 
  public function hasAccount($accountNumber) 
  { 
    return array_key_exists($accountNumber, $this->accounts); 
  } 
 
  public function getAccount($accountNumber) 
  { 
    if (!$this->hasAccount($accountNumber)) 
    { 
      throw new NoSuchAccountException(accountNumber); 
    } 
    return $this->accounts[$accountNumber]; 
  } 
 
  public function openSavingsAccount($number, Owner $owner) 
  { 
    if ($this->hasAccount($number)) 
    { 
      return null; 
    } 
 
    $account = new SavingsAccount($number, $owner); 
    $this->accounts[$number] = $account; 
    return $account; 
  } 
 
  public function openCheckingAccount($number, Owner $owner) 
  { 
    if ($this->hasAccount($number)) 
    { 
      return null; 
    } 
 
    $account = new CheckingAccount($number, $owner); 
    $this->accounts[$number] = $account; 
    return $account; 
  } 
 
  public function deposit(Money $amount, $number) 
  { 
    $account = $this->accounts[$number]; 
    return $account->deposit($amount); 
  } 
 
  public function withdraw(Money $amount, $number, WithdrawType $type) 
  { 
    $account = $this->accounts[$number]; 
    return $account->withdraw($amount, $type); 
  } 
 
  public function freezeAccount($number) 
  { 
    $account = $this->accounts[$number]; 
    $account->freeze(); 
  } 
 
  public function getAccounts() 
  { 
	$l = array();
	foreach($this->accounts as $account) {
		array_push($l, $account);
	}
	return $l;
  } 
 
  public function transfer($numberFrom, $numberTo, Money $amountToTransfert) 
  { 
    if (!$this->hasAccount($numberFrom)) 
    { 
      throw new NoSuchAccountException($numberFrom); 
    } 
    if (!$this->hasAccount($numberTo)) 
    { 
      throw new NoSuchAccountException($this->numberTo); 
    } 
 
    $accountFrom = $this->accounts[$numberFrom]; 
    $accountTo = $this->accounts[$numberTo]; 
 
    if ($accountFrom->getOwner()->getFirstName() == $accountTo->getOwner()->getFirstName() 
      && $accountFrom->getOwnerName() == $accountTo->getOwnerName()) 
    { 
      $accountFrom->withdrawAmmount($amountToTransfert); 
      $accountTo->deposit($amountToTransfert); 
    } 
    else 
    { 
      throw new Exception("Can't transfer from not owned account !"); 
    } 
  } 
} 