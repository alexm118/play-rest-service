name := """play-rest-service"""
organization := "com.alexmartin"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

coverageEnabled := true
coverageMinimum := 50
coverageFailOnMinimum := false

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.alexmartin.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.alexmartin.binders._"
