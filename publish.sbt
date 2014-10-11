import bintray.Keys._

bintraySettings

repository in bintray := "maven"

name in bintray := "rxjava-libgdx"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

pomExtra :=
  <url>https://github.com/jmhofer/rxjava-libgdx</url>
    <scm>
      <url>git@github.com:jmhofer/rxjava-libgdx.git</url>
      <connection>scm:git:git@github.com:jmhofer/rxjava-libgdx.git</connection>
    </scm>
    <developers>
      <developer>
        <id>jmhofer</id>
        <name>Joachim Hofer</name>
        <email>jmhofer.github@johoop.de</email>
      </developer>
      <developer>
        <id>cesarizu</id>
        <name>César Izurieta</name>
        <email>cesar@caih.org</email>
      </developer>
    </developers>
