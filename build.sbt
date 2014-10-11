version := "0.1"

name := "rxjava-libgdx"

organization := "de.johoop"

libraryDependencies ++= Seq(
  "io.reactivex" % "rxjava" % "1.0.0-rc.6",
  "com.badlogicgames.gdx" % "gdx" % "1.4.1",
  "com.badlogicgames.gdx" % "gdx-box2d" % "1.4.1",
  "com.novocode" % "junit-interface" % "0.9" % "test",
  "org.mockito" % "mockito-core" % "1.10.8" % "test")
