#!/bin/sh

UMLG_HOST="127.0.0.1"
umlgserverpid=0
UNAME=`which uname`
GREP=`which egrep`
GREP_OPTIONS=""
CUT=`which cut`
READLINK=`which readlink`
MKTEMP=`which mktemp`
RM=`which rm`
CAT=`which cat`
TR=`which tr`

start()
{
  START=$(date +%s)
  status
  #statusResult=$?
  statusResult=$umlgserverpid
  if [ $statusResult -eq 0 ]; then

    # ---------------------------------------------------------------------
    # Locate a JDK installation directory which will be used to run the IDE.
    # Try (in order): IDEA_JDK, JDK_HOME, JAVA_HOME, "java" in PATH.
    # ---------------------------------------------------------------------
    if [ -n "$IDEA_JDK" -a -x "$IDEA_JDK/bin/java" ]; then
      JDK="$IDEA_JDK"
    elif [ -n "$JDK_HOME" -a -x "$JDK_HOME/bin/java" ]; then
      JDK="$JDK_HOME"
    elif [ -n "$JAVA_HOME" -a -x "$JAVA_HOME/bin/java" ]; then
      JDK="$JAVA_HOME"
    else
      JAVA_BIN_PATH=`which java`
      if [ -n "$JAVA_BIN_PATH" ]; then
        if [ "$OS_TYPE" = "FreeBSD" ]; then
          JAVA_LOCATION=`JAVAVM_DRYRUN=yes java | "$GREP" '^JAVA_HOME' | "$CUT" -c11-`
          if [ -x "$JAVA_LOCATION/bin/java" ]; then
            JDK="$JAVA_LOCATION"
          fi
        elif [ "$OS_TYPE" = "SunOS" ]; then
          JAVA_LOCATION="/usr/jdk/latest"
          if [ -x "$JAVA_LOCATION/bin/java" ]; then
            JDK="$JAVA_LOCATION"
          fi
        elif [ "$OS_TYPE" = "Darwin" ]; then
          JAVA_LOCATION=`/usr/libexec/java_home`
          if [ -x "$JAVA_LOCATION/bin/java" ]; then
            JDK="$JAVA_LOCATION"
          fi
        fi

        if [ -z "$JDK" -a -x "$READLINK" ]; then
          JAVA_LOCATION=`"$READLINK" -f "$JAVA_BIN_PATH"`
          case "$JAVA_LOCATION" in
            */jre/bin/java)
              JAVA_LOCATION=`echo "$JAVA_LOCATION" | xargs dirname | xargs dirname | xargs dirname` ;;
            *)
              JAVA_LOCATION=`echo "$JAVA_LOCATION" | xargs dirname | xargs dirname` ;;
          esac
          if [ -x "$JAVA_LOCATION/bin/java" ]; then
            JDK="$JAVA_LOCATION"
          fi
        fi
      fi
    fi

    if [ -z "$JDK" ]; then
      echo "ERROR: cannot start Umlg Server."
      echo "No JDK found. Please validate either IDEA_JDK, JDK_HOME or JAVA_HOME environment variable points to valid JDK installation."
      echo
      echo "Press Enter to continue."
      read IGNORE
      exit 1
    fi

    echo "JDK = $JDK"
    echo 'starting etl server'

    SCRIPT_LOCATION=$0
    if [ -x "$READLINK" ]; then
      while [ -L "$SCRIPT_LOCATION" ]; do
        SCRIPT_LOCATION=`"$READLINK" -e "$SCRIPT_LOCATION"`
      done
    fi

    echo "SCRIPT_LOCATION = $SCRIPT_LOCATION"

    UMLG_HOME=`dirname "$SCRIPT_LOCATION"`/..
    UMLG_BIN_HOME=`dirname "$SCRIPT_LOCATION"`

    echo "UMLG_HOME = $UMLG_HOME"
    echo "UMLG_BIN_HOME = $UMLG_BIN_HOME"

    VM_OPTIONS_FILE="$UMLG_BIN_HOME/umlg.vmoptions"


    if [ -r "$VM_OPTIONS_FILE" ]; then
      VM_OPTIONS=`"$CAT" "$VM_OPTIONS_FILE" | "$GREP" -v "^#.*" | "$TR" '\n' ' '`
    fi

    ALL_JVM_ARGS="$VM_OPTIONS"

    echo "ALL_JVM_ARGS = $ALL_JVM_ARGS"

    MAIN_CLASS_NAME="org.umlg.demo.JettyDistributionDemo"

    echo "MAIN_CLASS_NAME = $MAIN_CLASS_NAME"

    CLASSPATH="$UMLG_HOME/demo-application.jar"
    CLASSPATH="$CLASSPATH:$UMLG_HOME/lib/*"
    export CLASSPATH

    echo "CLASSPATH = $CLASSPATH"

    # ---------------------------------------------------------------------
    # Run the server.
    # ---------------------------------------------------------------------
    nohup $JDK/bin/java -DUMLGServerDistribution=true $ALL_JVM_ARGS -classpath $CLASSPATH $MAIN_CLASS_NAME &
    #eval "$JDK/bin/java" -DEtlServerDistribution=true $ALL_JVM_ARGS -classpath $CLASSPATH $MAIN_CLASS_NAME $* &

  fi
  statusResult=1
  while [ $statusResult -ne 0 ]; do
    sleep 60
    status
    #statusResult=$?
    statusResult=$umlgserverpid
    END=$(date +%s)
    elapsed="$(expr $END - $START)"
    remainder="$(expr $elapsed % 3600)"
    hours="$(expr $(expr $elapsed - $remainder) / 3600)"
    seconds="$(expr $remainder % 60)"
    minutes="$(expr $(expr $remainder - $seconds) / 60)"
    dayRemainder="$(expr $hours % 24)"
    days="$(expr $(expr $hours - $dayRemainder) / 24)"
    dayHours="$(expr $days \* 24)"
    hours="$(expr $hours - $dayHours)"
    echo "Elapsed time: $days day(s) $hours hour(s) $minutes minute(s) $seconds second(s)"
  done
  #Sleep here to give stop script time to exit
  echo 'umlg server will start in 10 seconds'
  sleep 10
  start
}

stop()
{
  # This will the umlgserver java process.
  status
  #umlgserverpid=$?
  if [ $umlgserverpid -ne 0 ]; then
    echo 'stopping umlg server '$umlgserverpid
    kill $umlgserverpid
    counter=1
    umlgserverpid=1
    while [ $umlgserverpid -ne 0 ]; do
      sleep 5
      status
      #umlgserverpid=$?
      counter=`expr $counter + 1`
      if [ $umlgserverpid -ne 0 ]; then
        echo $counter' of 6 before force kill -9' $umlgserverpid
        if [ $counter -gt 6 ]; then
          echo 'force killing etl server, waited 1 minute for shutdown to complete'
          echo 'kill -9 '$umlgserverpid
          kill -9 $umlgserverpid
          status
        fi
      fi
    done
  else
    echo 'umlg server is not running'
  fi
}

stopscript()
{
  SCRIPTPID=$( ps aux | grep umlgserver.sh\ start | grep -v grep | awk '{print $2}' )
  if [ -n "$SCRIPTPID" ]; then
    echo 'kill -9 '$SCRIPTPID
    kill -9 $SCRIPTPID
  else
    echo 'script umlgserver.sh is not running'
  fi
}

stopall()
{
  stopscript
  stop
}

restart()
{
  stopscript
  stop
  start
}

status()
{
  umlgserverpid=$( ps aux | grep org.umlg.demo.JettyDistributionDemo | grep -v grep | awk '{print $2}' )
  if [ -n "$umlgserverpid" ]; then
    echo "umlg server (pid $umlgserverpid) is running"
    umlgserverpid=$umlgserverpid
    #return $umlgserverpid
  else
    echo 'umlg server is not running'
    umlgserverpid=0
    #return 0
  fi
}

case "$1" in
start)
    start
    ;;
stop)
    stop
    ;;
status)
    status
    ;;
stopscript)
    stopscript
    ;;
stopall)
    stopall
    ;;
restart)
    restart
    ;;
esac

echo "usage: $0 (start|stop|stopscript|stopall|restart|help)"