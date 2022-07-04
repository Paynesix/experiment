#!/bin/bash
chmod 775 /exp/experiment-0.0.1-SNAPSHOT.jar
nohup java -jar /exp/experiment-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
PIDS=`ps -ef |grep java |grep /exp |awk '{print $2}'`
echo "OK!"
echo "PID:$PIDS"
