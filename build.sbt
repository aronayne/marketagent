name := "marketagent"
 
version := "1.0" 
      
lazy val `marketagent` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws  , guice , specs2 % Test)

// https://mvnrepository.com/artifact/com.typesafe.scala-logging/scala-logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

libraryDependencies += "com.softwaremill.sttp.client" %% "core" % "2.1.5"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % "test"
)

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.1"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.1"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.5"

//libraryDependencies += "cn.playscala" % "play-mongo_2.12" % "0.3.0"
//
//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

//libraryDependencies += "cn.playscala" % "play-mongo_2.12" % "0.3.0"
//
//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
