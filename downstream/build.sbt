name         := "downstream"
organization := "net.paoloambrosio.sysintsim"
version      := "0.1"
scalaVersion := "2.11.5"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.3.9"
  val akkaStreamV = "1.0-M2"
  Seq(
    "com.typesafe.akka"    %% "akka-actor"                        % akkaV,
    "com.typesafe.akka"    %% "akka-stream-experimental"          % akkaStreamV,
    "com.typesafe.akka"    %% "akka-http-core-experimental"       % akkaStreamV,
    "com.typesafe.akka"    %% "akka-http-experimental"            % akkaStreamV,
    "com.typesafe.akka"    %% "akka-http-spray-json-experimental" % akkaStreamV
  )
}

libraryDependencies ++= Seq(
  "org.jmxtrans.embedded" % "embedded-jmxtrans" % "1.0.14"
)
