#!/bin/bash

gradle build
docker rmi my-downstream-spring
docker build --rm=true -t my-downstream-spring .
