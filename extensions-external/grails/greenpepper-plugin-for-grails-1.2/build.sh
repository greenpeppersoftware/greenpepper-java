#!/bin/sh
VERSION=$GREENPEPPER_VERSION
GRAILS_HOME=/opt/grails/grails-1.2
export GRAILS_HOME
cp ~/.m2/repository/greenpepper-open/greenpepper-core/$VERSION/greenpepper-core-$VERSION.jar lib
cp ~/.m2/repository/greenpepper-open/greenpepper-extensions-grails/$VERSION/greenpepper-extensions-grails-$VERSION.jar lib
cp ~/.m2/repository/greenpepper-open/greenpepper-extensions-java/$VERSION/greenpepper-extensions-java-$VERSION.jar lib
rm grails-greenpepper-0.1.zip
$GRAILS_HOME/bin/grails package-plugin
