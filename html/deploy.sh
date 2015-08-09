#!/bin/sh
rsync -a --delete --exclude /WEB-INF war/ ../../vograbulary-gh-pages/run
