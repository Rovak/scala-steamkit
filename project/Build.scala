import sbtprotobuf.{ProtobufPlugin => PB}

//import scalabuff.ScalaBuffPlugin._

import sbt._
import Keys._
import sbtassembly.Plugin._


object Tasks {
  /**
   * Generates the steam language
   */
  val generateSteamLanguage = TaskKey[Unit]("steam-generate-language", "Generates the language for steam") := {


  }
}

object SbtProject extends Build {

  val projectVersion = "0.1-SNAPSHOT"

  val defaultSettings = Project.defaultSettings ++ Seq(
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    version := projectVersion,
    scalaVersion := "2.10.2")


  import Dependencies._

  lazy val steamkit = Project(
    id = "steamkit",
    base = file("steamkit"),
    settings = defaultSettings ++ PB.protobufSettings ++ Seq(
      libraryDependencies ++= Seq(
        Apache.io,
        Apache.httpcore,
        Apache.lang,
        Apache.http,
        Akka.actor,
        Akka.testkit,
        Play.json,
        Google.gson,
        Unittests.scalatest,
        Google.protobuf,
        Etc.bouncy,
        Etc.ombok)
    )
  ).dependsOn(steam)

  lazy val steam = Project(
    id = "steam",
    base = file("steam"),
    // scalabuffSettings
    settings = defaultSettings ++ PB.protobufSettings ++ assemblySettings++ Seq(
      libraryDependencies ++= Seq(
        Google.protobuf,
        Etc.bouncy,
        Etc.ombok)
    ) ++ seq(Tasks.generateSteamLanguage)
  )

  lazy val projectRoot = Project(
    id = "projectRoot",
    base = file("."),
    settings = defaultSettings
  )

}
