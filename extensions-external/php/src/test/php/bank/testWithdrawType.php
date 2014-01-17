<?php

include_once dirname(__FILE__).'/WithdrawType.php';


assert(WithdrawType::$ATM->toString() == 'ATM');
assert(WithdrawType::$ATM->getId() == 0);
assert(WithdrawType::$INTERAC->toString() == 'Interact');
assert(WithdrawType::$INTERAC->getId() == 1);
assert(WithdrawType::$PERSONAL_CHECK->toString() == 'Personal Check');
assert(WithdrawType::$PERSONAL_CHECK->getId() == 2);