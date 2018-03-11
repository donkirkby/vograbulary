#!/bin/sh
rsync -a --delete --exclude /WEB-INF war/run ../build/classes/artifacts/html_GWT/vograbulary war/images war/stylesheets ../docs
