#!/bin/bash

set -e 

mvn clean package
docker build -t loraneo/cdr-gen:1.0.0a .