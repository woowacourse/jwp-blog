#!/bin/bash

# PUT YOUR BRANCH NAME
BRANCH='andole87'

echo ">>> GIT PULL FROM BRANCH $BRANCH ..."
git pull origin $BRANCH

echo ">>> BUILD START ..."
gradlew clean build
echo "<<< BUILD COMPLETE ..."

CURRENT_PID=$(pgrep -f spring)

echo ">>> Process $CURRENT_PID Will Killed ..."
kill -2 $CURRENT_PID

echo ">>> NEW PROCESS START ..."
java -jar build/libs/*.jar &
