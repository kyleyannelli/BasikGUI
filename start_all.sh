#!/bin/sh
j_pid=$(
  cd java
  mvn clean javafx:run > /dev/null
  java_pid=$!
  echo $java_pid
) &
p_pid=$(
  sleep 2
  cd python
  ./start_basik.sh
) &
wait $j_pid
./stop_backend.sh > /dev/null
pkill -f p_pid > /dev/null
