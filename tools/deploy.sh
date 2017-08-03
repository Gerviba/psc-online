#!/bin/bash
TOMCAT_ROOT=~/apps/tomcat8.5
if netstat -nl | grep ":8080" -q; then
	echo "Stopping..."
	sh $TOMCAT_ROOT/bin/catalina.sh stop
fi
echo "Copying WAR..."
cp target/psc-online.war $TOMCAT_ROOT/webapps/ROOT.war
echo "Starting..."
#Use `run` insted of `start` to debug
$TOMCAT_ROOT/bin/catalina.sh run
