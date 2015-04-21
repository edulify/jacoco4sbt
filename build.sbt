sbtPlugin := true

name := "jacoco4sbt"

organization := "de.johoop"

version := "2.1.8.Edulify"

resolvers += "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

scalaVersion := "2.10.5"

val jacocoCore = Artifact("org.jacoco.core", "jar", "jar")

val jacocoReport = Artifact("org.jacoco.report", "jar", "jar")

val jacocoVersion = "0.7.4.201502262128"

val specs2Version = "3.4"

libraryDependencies ++= Seq(
  "org.jacoco"  %  "org.jacoco.core"      % jacocoVersion artifacts jacocoCore,
  "org.jacoco"  %  "org.jacoco.report"    % jacocoVersion artifacts jacocoReport,
  "org.specs2"  %% "specs2-core"          % specs2Version % Test,
  "org.specs2"  %% "specs2-junit"         % specs2Version % Test,
  "org.specs2"  %% "specs2-mock"          % specs2Version % Test,
  "org.specs2"  %% "specs2-matcher-extra" % specs2Version % Test,
  "org.mockito" %  "mockito-all"          % "1.10.19" % Test,
  "org.pegdown" %  "pegdown"              % "1.5.0"   % Test
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_")

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](resourceDirectory in Test)

buildInfoPackage := "de.johoop.jacoco4sbt.build"

test in Test <<= test in Test dependsOn publishLocal

parallelExecution in Test := false
