@echo off

SET PHP=c:/opt/php/php.exe
SET PHP_SRC=src/test/php
SET PHP_INIT_FILE=init.php

SET CMD=java -cp "target/greenpepper-extensions-php-3.0-jar-with-dependencies.jar" com.greenpepper.phpsud.Runner %PHP% %PHP_SRC% %PHP_INIT_FILE% -o target

%CMD% -s src/test/specs
