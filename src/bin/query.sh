bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin"; pwd`

if [ $# = 0 ]; then
  echo "Usage: query.sh input output query"
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

CLASSPATH="${QCT_HOME}/conf"
CLASSPATH=${CLASSPATH}:"$QCT_HOME/lib/*"

CLASS='com.imaginea.qctree.hadoop.QueryDriver'

exec "$JAVA" -classpath "$CLASSPATH" $CLASS "$@"

