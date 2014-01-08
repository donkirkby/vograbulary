#!/bin/sh
java -Dfile.encoding=UTF-8 -classpath \
bin:\
libs/junit-4.11.0.jar:\
libs/hamcrest.core-1.3.0.jar:\
../vograbulary/bin:\
../vograbulary/libs/gdx.jar:\
libs/mockito-all-1.9.5.jar:\
../vograbulary/libs/gdx-tools.jar \
org.junit.runner.JUnitCore com.github.donkirkby.vograbulary.AllTests
