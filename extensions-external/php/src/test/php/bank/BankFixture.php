<?php

include_once dirname(__FILE__).'/Bank.php';

class BankFixture 
{ 
 
  private $bank; 
 
  public function BankFixture() 
  { 
    $this->bank = new Bank(); 
  } 
 
  public function openSavingsAccountUnderTheNameOf($number, $firstName, $lastName) 
  { 
    return $this->bank->openSavingsAccount($number, new Owner($firstName, $lastName)) != null; 
  } 
 
  public function openCheckingAccountUnderTheNameOf($number, $firstName, $lastName) 
  { 
    return $this->bank->openCheckingAccount($number, new Owner($firstName, $lastName)) != null; 
  } 
 
  public function openAccountUnderTheNameOf(AccountType $type, $number, $firstName, $lastName) 
  { 
    if (AccountType::$SAVINGS == $type) 
    { 
      return $this->openSavingsAccountUnderTheNameOf($number, $firstName, $lastName); 
    } 
    else if (AccountType::$CHECKING == $type) 
    { 
      return $this->openCheckingAccountUnderTheNameOf($number, $firstName, $lastName); 
    } 
    return false; 
  } 
 
  public function thatBalanceOfAccountIs($accountNumber) 
  { 
    $account = $this->bank->getAccount($accountNumber); 
    return $account->getBalance(); 
  } 
 
  public function depositInAccount(Money $amount, $accountNumber) 
  { 
    try 
    { 
      $this->bank->deposit($amount, $accountNumber); 
      return true; 
    } 
    catch (Exception $e) 
    { 
      return false; 
    } 
  } 
 
  public function withdrawFromAccount(Money $amount, $accountNumber) 
  { 
    return $this->withdrawFromAccountUsing($amount, $accountNumber, WithdrawType::$ATM); 
  } 
 
  public function withdrawFromAccountUsing(Money $amount, $accountNumber, WithdrawType $withdrawType) 
  { 
    try 
    { 
      $this->bank->withdraw($amount, $accountNumber, $withdrawType); 
    } 
    catch (Exception $e) 
    { 
      return false; 
    } 
    return true; 
  } 
 
  public function getOpenedAccounts() 
  { 
    return $this->bank->getAccounts(); 
  } 
 
  public function freezeAccount($accountNumber) 
  { 
    $this->bank->freezeAccount($accountNumber); 
  } 
 
  public function createAccountForWithBalanceOf(AccountType $type, $number, $firstName, $lastName, 
                         Money $balance) 
  { 
    $account = ($type == AccountType::$SAVINGS) ? 
                $this->bank->openSavingsAccount($number, new Owner($firstName, $lastName)) : 
                $this->bank->openCheckingAccount($number, new Owner($firstName, $lastName)); 
 
    $account->deposit($balance); 
    return true; 
  } 
 
  public function transferFromAccountToAccount(Money $amountToTransfer, $fromAccountNumber, 
                        $toAccountNumber) 
  { 
    try 
    { 
      $this->bank->transfer($fromAccountNumber, $toAccountNumber, $amountToTransfer); 
    } 
    catch (Exception $e) 
    { 
      return false; 
    } 
    return true; 
  } 
} 