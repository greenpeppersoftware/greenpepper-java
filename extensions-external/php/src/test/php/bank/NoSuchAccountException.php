<?php


class NoSuchAccountException 
{ 
 
  private $number; 
 
  public function NoSuchAccountException($number) 
  { 
    $this->number = $number; 
  } 
 
  public function getNumber() 
  { 
    return $this->number; 
  } 
 
  public function getMessage() 
  { 
    return "Account does not exist: ".$this->number; 
  } 
} 