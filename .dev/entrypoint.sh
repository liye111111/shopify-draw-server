#!/bin/sh
set -e

export mode=${mode}
export profile=${profile}
echo "profile: ${profile}, mode=${mode}, zookeeper: ${zk_address}, APP_NAME=${APP_NAME}"

APP_OPTS="-Dapp.name=${APP_NAME}"
LOG_OPTS="-Dlog.dir=${HOME}/logs"
PROFILE_OPTS="-Dspring.profiles.active=$profile"
SECURITY_OPTS="-Djava.security.egd=file:/dev/./urandom"

start(){
	# java opts
	JAVA_OPTS="${JAVA_OPTS} $LOG_OPTS $APP_OPTS $PROFILE_OPTS $SECURITY_OPTS"
	JAVA_OPTS="${JAVA_OPTS} -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
	JAVA_OPTS="${JAVA_OPTS} --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.security=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED --add-opens java.base/java.time=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/jdk.internal.access=ALL-UNNAMED --add-opens java.base/jdk.internal.misc=ALL-UNNAMED --add-opens java.sql/java.sql=ALL-UNNAMED --add-opens java.sql/javax.sql=ALL-UNNAMED"

#JDK17
  JAVA_OPTS="${JAVA_OPTS} -server -Xms512m -Xmx1536m -XX:MaxDirectMemorySize=512m -XX:+UseZGC -Xlog:gc*:$HOME/gc.log -XX:+DisableExplicitGC "
  JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HOME/errorDump.hprof "

  if [ -f /opentelemetry-javaagent.jar ]; then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/opentelemetry-javaagent.jar"
  fi

  JAVA_OPTS="${JAVA_OPTS} -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=0.0.0.0:18000,server=y,suspend=n "

	echo "Starting the Server ..."
	exec java $JAVA_OPTS -jar /app.jar
}

start | tee /var/log/app.log
