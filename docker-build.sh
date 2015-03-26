#!/bin/bash

function build {
  cd $1; ./docker-build.sh; cd -
}

build upstream
build downstream-spring
build perftest
build grafana
