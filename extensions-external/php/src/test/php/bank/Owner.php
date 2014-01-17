<?php

class Owner
{

  private $firstName;
  private $lastName;

  public function Owner($firstName, $lastName)
  {
    $this->firstName = $firstName;
    $this->lastName = $lastName;
  }

  public function getFirstName()
  {
    return $this->firstName;
  }

  public function getLastName()
  {
    return $this->lastName;
  }

  public function getFullName()
  {
    return $this->firstName." ".$this->lastName;
  }
}