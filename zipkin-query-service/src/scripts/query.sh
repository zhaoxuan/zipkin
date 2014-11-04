#!/bin/sh
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
STORAGE=${1:-"cassandra"}
VERSION=@VERSION@
CONFIG=${DIR}/../config/query-${STORAGE}.scala
nohup java -Xmx40960m -Xms40960m -Xmn10240m -XX:+PrintGC -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationConcurrentTime -XX:+UseG1GC -Xloggc:log/gc.log -Djute.maxbuffer=10240000 -cp "$DIR/../libs/*" -jar $DIR/../zipkin-query-service-$VERSION.jar -f ${CONFIG} 1>/dev/null 2>&1 &