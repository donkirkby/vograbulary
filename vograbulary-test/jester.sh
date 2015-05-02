#!/bin/sh
export CLASSPATH=\
~/Downloads/simple-jester120
java -Dfile.encoding=UTF-8 -jar ~/Downloads/simple-jester120/simple-jester.jar \
-buildCommand "./build.sh" \
-source ../core/src/com/github/donkirkby/vograbulary \
-shouldShowProgressDialog true
