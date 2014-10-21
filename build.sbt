version := "0.2"

name := "rxjava-libgdx"

organization := "de.johoop"

libraryDependencies ++= Seq(
  "io.reactivex" % "rxjava" % "1.0.0-rc.6", // the version currently supported by RxScala
  "com.badlogicgames.gdx" % "gdx" % "1.4.1",
  "com.badlogicgames.gdx" % "gdx-box2d" % "1.4.1",
  "com.novocode" % "junit-interface" % "0.9" % "test",
  "org.mockito" % "mockito-core" % "1.10.8" % "test")

javacOptions in Compile ++= Seq("-source", "1.7", "-target", "1.7")

javacOptions in (Compile, doc) := Seq("-source", "1.7")

crossVersion := CrossVersion.Disabled
