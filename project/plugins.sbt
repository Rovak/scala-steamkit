resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin(dependency="com.github.mpeltonen" % "sbt-idea" % "1.6.0")

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0"

addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.3.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.2")

