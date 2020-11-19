#!/bin/bash

compile () {
mkdir -p app1lib
mkdir -p app2lib
javac -cp bin -d app1lib/  app1src/org/jainy/application/*.java
javac -cp bin -d app2lib/  app2src/org/jainy/application/*.java
}

compile
#java -cp bin org.jainy.server.AppServer -DclassLoader.childFirst=true app1=org.jainy.application.App1 app2=org.jainy.application.App2