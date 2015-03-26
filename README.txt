How to run in Docker:
 $ docker-build.sh
 - Point the Web browser to http://localhost/ to see the dashboard
 $ docker-system-up.sh
 $ docker-run-perftest.sh
 $ docker-system-down.sh # When done

How to run without Docker:
 $ cd grafana; ./docker-build.sh; cd -; docker run --rm -p 80:80 -p 2003:2003 --name grafana my-grafana
 - Point the Web browser to http://localhost/ to see the dashboard
 $ cd upstream; sbt run
 $ cd downstream-spring; gradle run
 $ cd perftest; sbt test

To change the upstream slowdown distribution:
 $ sbt -Dapplication.slowdown-strategy.distribution=constant2s run

To run Gatling on the upstream only:
 $ sbt -Dundertest.port=9000 test

