#!/bin/bash
set -e

HOST_IP=$(ifconfig eth1 | grep 'inet addr' | cut -d: -f2 | awk '{print $1}')
echo "Host IP: ${HOST_IP}"
DUBBO_IP_OPTS="-DDUBBO_IP_TO_BIND=${HOST_IP} -DUBBO_IP_TO_REGISTRY=${HOST_IP} -Ddubbo.network.interface.preferred=eth1"

# export MODE="cluster"
export mode=${mode}
export profile=${profile}
echo "profile: ${profile}, mode=${mode}, ZK: ${zk_address}"

JVM_MEM="4G"
APP_OPTS="-Dapp.name=${APP_NAME}"
LOG_OPTS="-Dlog.dir=${HOME}/logs"
PROFILE_OPTS="-Dspring.profiles.active=$profile"
SECURITY_OPTS="-Djava.security.egd=file:/dev/./urandom"

start(){
	# java opts
	JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
	JAVA_DEBUG_OPTS=""
	if [ "${mode}" = "debug" ]; then
	    JAVA_DEBUG_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=18000,server=y,suspend=n "
	fi

	# java mem opts
	JAVA_MEM_OPTS=""
	if [[ "${profile}" =~ "prod" ]]; then
		JAVA_MEM_OPTS="-server -Xmx$JVM_MEM -Xms$JVM_MEM -Xmn1G -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
	else
		JAVA_MEM_OPTS="-Xmx1740m -Xms1740m -Xmn256m"
	fi

	# Don't force Dubbo IP in local development
	if [ "${profile}" = "dev" ]; then
		DUBBO_IP_OPTS=""
	fi

	echo "Starting the Server ..."
	echo "............"
	echo "exec java $DUBBO_IP_OPTS $JAVA_OPTS $JAVA_MEM_OPTS $LOG_OPTS $APP_OPTS $JAVA_DEBUG_OPTS $PROFILE_OPTS $SECURITY_OPTS -jar /app.jar"
	echo "............"
	exec java $DUBBO_IP_OPTS $JAVA_OPTS $JAVA_MEM_OPTS $LOG_OPTS $APP_OPTS $JAVA_DEBUG_OPTS $PROFILE_OPTS $SECURITY_OPTS -jar /app.jar
}

start