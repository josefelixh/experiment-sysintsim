#!/bin/bash

sbt stage
docker rmi my-upstream
docker build --rm=true -t my-upstream .
