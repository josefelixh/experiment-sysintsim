#!/bin/bash

docker run --rm --link grafana:graphite --link downstream:undertest my-perftest

