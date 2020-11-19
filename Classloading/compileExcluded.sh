#!/bin/bash

compile () {
mkdir -p binExclude
javac -d binExclude/  srcExclude/org/jainy/exclude/*.java
}

compile