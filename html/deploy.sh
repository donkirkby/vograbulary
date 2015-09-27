#!/bin/sh
rsync -a --delete --exclude /WEB-INF war/run war/vograbulary_html war/images war/stylesheets ../../vograbulary-gh-pages
