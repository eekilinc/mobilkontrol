#!/bin/sh
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
exec java $DEFAULT_JVM_OPTS -jar gradle/wrapper/gradle-wrapper.jar "$@"
