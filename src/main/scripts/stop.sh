#!/bin/bash
cd /exp
SERVER_NAME="exp-web"

if [ -z "$SERVER_NAME" ]; then
  SERVER_NAME=`hostname`
fi

PIDS=`ps -ef | grep java | grep /exp |awk '{print $2}'`
if [ -z "$PIDS" ]; then
  echo "[ERROR] The $SERVER_NAME does not started!"
  exit 1
fi

echo -e "Stopping the $SERVER_NAME ...\c"
for PID in $PIDS; do
  kill $PID > /dev/null 2>&1
done

echo "OK!"
echo "PID:$PIDS"
