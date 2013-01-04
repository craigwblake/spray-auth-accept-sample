seq(com.typesafe.sbt.SbtStartScript.startScriptForClassesSettings: _*)

scalacOptions += "-Ydependent-method-types"

name := "test-project"

version := "1.0"

scalaVersion := "2.9.2"

resolvers += Resolver.sonatypeRepo("scala-tools")

resolvers += Resolver.sonatypeRepo("public")

resolvers += Resolver.sonatypeRepo("releases")

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += "Spray" at "http://repo.spray.io"

resolvers += "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "io.spray" % "spray-can" % "1.0-M7"

libraryDependencies += "io.spray" % "spray-http" % "1.0-M7"

libraryDependencies += "io.spray" % "spray-routing" % "1.0-M7"

libraryDependencies += "io.spray" %%  "spray-json" % "1.2.3" cross CrossVersion.full

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.4"
