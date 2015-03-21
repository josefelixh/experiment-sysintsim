package gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SystemSimulation extends Simulation {

  val host = System.getProperty("undertest.host", "localhost")
  val port = System.getProperty("undertest.port", "8080")

  val httpConf = http.baseURL(s"http://$host:$port")

  val scn = scenario("UpstreamSimulation")
    .forever { exec(http("OnlyRequest").get("/")) }

  setUp(
    scn
      .inject(atOnceUsers(2000))
      .throttle(
        reachRps(500) in (30 seconds),
        holdFor(90 seconds)
      )
  ).protocols(httpConf)

}