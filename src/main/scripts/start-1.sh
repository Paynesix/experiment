#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
DATE_DIR=`date +%Y-%m-%d`
LOG_FILE=$DEPLOY_DIR/logs/$DATE_DIR/experiment-info-log.log

#com/xy/experiment/ExperimentApplication.java
APPLICATION_CLASS="com.xy.experiment.ExperimentApplication"
# SERVER_NAME
SERVER_NAME="welife-admin-web"
if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`ps -ef | grep java | grep "$CONF_DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "[ERROR] The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

LOGS_DIR=""
if [ -n "$LOGS_FILE" ]; then
    LOGS_DIR=`dirname $LOGS_FILE`
else
    LOGS_DIR=$DEPLOY_DIR/logs
fi
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi


JAVA_OPTS=" -Djava.net.preferIPv4Stack=true -Dfile.encoding=utf-8"
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
	JAVA_MEM_OPTS=" -server -DJM.LOG.PATH=$LOGS_DIR  -XX:MaxDirectMemorySize=256M  -Xmx512M -Xms512M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M  -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark   -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../logs/dump.hprof -XX:+PrintGCDetails -Xloggc:./logs/gc.log -XX:+PrintGCTimeStamps "
else
    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

echo -e "Starting the $SERVER_NAME ..."

nohup java -Dapplication.name=$SERVER_NAME $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS    -classpath $DEPLOY_DIR/lib/sp-dubbo-2.3.0.jar:$DEPLOY_DIR/conf:$DEPLOY_DIR/lib/* $APPLICATION_CLASS > /dev/null 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "The $SERVER_NAME start success!"

echo -e "waiting to tail the log file $LOG_FILE "
while [ ! -e "$LOG_FILE" ]; do
    echo -e ".\c"
    sleep 1
done
echo ""

tail -f $LOG_FILE
