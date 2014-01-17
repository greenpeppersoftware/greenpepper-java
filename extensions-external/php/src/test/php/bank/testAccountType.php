<?php

include_once dirname(__FILE__).'/AccountType.php';


assert(AccountType::$SAVINGS->toString() == 'savings');
assert(AccountType::$SAVINGS->limitFor(WithdrawType::$ATM)->toString() == '$200.00');
assert(AccountType::$SAVINGS->limitFor(WithdrawType::$INTERAC)->toString() == '$200.00');
assert(AccountType::$SAVINGS->limitFor(WithdrawType::$PERSONAL_CHECK) === null);

assert(AccountType::$SAVINGS->feesFor(WithdrawType::$ATM)->toString() == '$1.50');
assert(AccountType::$SAVINGS->feesFor(WithdrawType::$INTERAC)->toString() == '$2.50');
assert(AccountType::$SAVINGS->feesFor(WithdrawType::$PERSONAL_CHECK)->toString() == '$0.50');


assert(AccountType::$CHECKING->toString() == 'checking');
assert(AccountType::$CHECKING->limitFor(WithdrawType::$ATM)->toString() == '$200.00');
assert(AccountType::$CHECKING->limitFor(WithdrawType::$INTERAC)->toString() == '$200.00');
assert(AccountType::$CHECKING->limitFor(WithdrawType::$PERSONAL_CHECK) === null);

assert(AccountType::$CHECKING->feesFor(WithdrawType::$ATM)->toString() == '$0.00');
assert(AccountType::$CHECKING->feesFor(WithdrawType::$INTERAC)->toString() == '$2.50');
assert(AccountType::$CHECKING->feesFor(WithdrawType::$PERSONAL_CHECK)->toString() == '$0.00');

assert(!AccountType::isNoLimit(Money::parse(1)));
assert(AccountType::isNoLimit(null));