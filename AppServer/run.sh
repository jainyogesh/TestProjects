#!/bin/bash

compile () {
mkdir -p app1lib
mkdir -p app2lib
javac -cp bin -d app1lib/  app1src/org/jainy/application/*.java
javac -cp bin -d app2lib/  app2src/org/jainy/application/*.java
}

compile
echo
echo
echo "****************************************"
echo "Running with child classloader first..."
java -cp bin -DclassLoader.childFirst=true org.jainy.server.AppServer app1=org.jainy.application.App1 app2=org.jainy.application.App2

echo
echo
echo "****************************************"
echo "Running with parent classloader first..."
java -cp bin -DclassLoader.childFirst=false org.jainy.server.AppServer app1=org.jainy.application.App1 app2=org.jainy.application.App2
