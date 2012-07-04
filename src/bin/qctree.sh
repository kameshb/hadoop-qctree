bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

if [ $# = 0 ]; then
  echo "Usage: qctree input output"
  exit 1
fi

if [ "$JAVA_HOME" != "" ]; then
  JAVA_HOME=$JAVA_HOME
fi
  
if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

JAVA=$JAVA_HOME/bin/java

QCT_HOME="$bin"/..

QCT_OPTS="-files=$QCT_HOME/conf/table.json"
QCT_OPTS="$QCT_OPTS -libjars=$QCT_HOME/lib/gson-2.2.1.jar"

CLASSPATH="${QCT_HOME}/conf"
CLASSPATH=${CLASSPATH}:"$QCT_HOME/lib/*"

CLASS='com.imaginea.qctree.hadoop.QCDriver'

exec "$JAVA" -classpath "$CLASSPATH" $CLASS $QCT_OPTS "$@"

