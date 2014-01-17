<?php

class Province {
  private $name;
  private $code;

  function Province($name, $code) {
    $this->name = $name;
    $this->code = $code;
  }

  function getName() {
    return $this->name;
  }

  function setName($name) {
    $this->name = $name;
  }

  function getCode()
  {
    return $this->code;
  }

  function setCode($code)
  {
    $this->code = $code;
  }
}
