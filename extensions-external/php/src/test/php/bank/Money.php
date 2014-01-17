<?php

include_once dirname(__FILE__).'/BigDecimal.php';

class Money
{
  public static $ZERO;
  
  private $dollars;

  public function Money(BigDecimal $dollars)
  {
	$this->dollars = $dollars;
  }

  public static function parse($text)
  {
    return new Money(new BigDecimal(Money::normalize($text)));
  }

  private static function normalize($text)
  {
    return preg_replace("/\\s/", "", preg_replace("/,/", "", preg_replace("/\\$/", "", $text)));
  }

  public static function dollars($amount)
  {
    return new Money(new BigDecimal($amount));
  }

  public static function zero()
  {
    return Money::dollars(0);
  }

  public function equals($other)
  {
    if (get_class($other) == "Money")
    {
      return $this->dollars->compareTo($other->dollars) == 0;
    }
    else
    {
      return false;
    }
  }
  
  public function toString()
  {
    return '$'.$this->dollars->toString();
  }

  public function times($multiplier)
  {
    return new Money($this->dollars->multiply($multiplier));
  }

  public function minus(Money $subtrahend)
  {
    return new Money($this->dollars->substract($subtrahend->dollars));
  }

  public function plus(Money $addend)
  {
    return new Money($this->dollars->add($addend->dollars));
  }

  public function greaterThan(Money $money)
  {
    return $this->dollars->compareTo($money->dollars) >= 0;
  }

  public function strictlyGreaterThan(Money $money)
  {
    return $this->dollars->compareTo($money->dollars) > 0;
  }

  public function lowerThan(Money $money)
  {
    return $this->dollars->compareTo($money->dollars) <= 0;
  }

  public function strictlyLowerThan(Money $money)
  {
    return $this->dollars->compareTo($money->dollars) < 0;
  }
  
  public static function init() {
    Money::$ZERO = Money::parse(0);
  }
}

Money::init();
