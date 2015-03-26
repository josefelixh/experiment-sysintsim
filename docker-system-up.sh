#!/bin/bash

docker run -d -p 80:80 --expose=2003 --name grafana my-grafana
docker run -d --name upstream --link grafana:graphite my-upstream
docker run -d --name downstream --link grafana:graphite --link upstream:upstream my-downstream-spring

