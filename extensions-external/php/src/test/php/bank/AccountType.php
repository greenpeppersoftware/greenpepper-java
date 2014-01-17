<?php

include_once dirname(__FILE__).'/WithdrawType.php';
include_once dirname(__FILE__).'/Money.php';

class AccountType 
{ 
 
  public static $SAVINGS;
  public static $CHECKING;
  
  private $description; 
 
  private $limits = array(); 
  private $fees = array(); 
 
  private function AccountType($description, Money $limitBankMachine = NULL, 
            Money $limitInteracMachine = NULL, Money $limitPersonalCheck = NULL, 
            Money $feesBankMachine = NULL, Money $feesInteracMachine = NULL, Money $feesPersonalCheck = NULL) 
  { 
    $this->description = $description; 
    $this->limits[WithdrawType::$ATM->getId()] = $limitBankMachine; 
    $this->limits[WithdrawType::$INTERAC->getId()] = $limitInteracMachine; 
    $this->limits[WithdrawType::$PERSONAL_CHECK->getId()] = $limitPersonalCheck; 
 
    $this->fees[WithdrawType::$ATM->getId()] = $feesBankMachine; 
	$this->fees[WithdrawType::$INTERAC->getId()] = $feesInteracMachine; 
	$this->fees[WithdrawType::$PERSONAL_CHECK->getId()] = $feesPersonalCheck; 
  } 
  
  public function toString() 
  { 
    return $this->description; 
  } 
 
  public function limitFor(WithdrawType $withdrawType) 
  { 
    return $this->limits[$withdrawType->getId()]; 
  } 
  
  public function feesFor(WithdrawType $withdrawType) 
  { 
    return $this->fees[$withdrawType->getId()]; 
  } 
 
  public static function isNoLimit(Money $limit = NULL) 
  { 
    return $limit == null; 
  } 
  
  public static function init() {
	AccountType::$SAVINGS = new AccountType("savings", Money::parse(200), Money::parse(200), null, Money::parse(1.5), Money::parse(2.5), Money::parse(0.5));
	AccountType::$CHECKING = new AccountType("checking", Money::parse(200), Money::parse(200), null, Money::parse(0), Money::parse(2.50), Money::parse(0)); 
  }
} 

AccountType::init();
