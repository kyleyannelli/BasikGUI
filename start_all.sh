#!/bin/sh
(
  cd java
  mvn clean javafx:run > /dev/null
) &
(
  sleep 2
  cd python
  ./start_basik.sh
) &
