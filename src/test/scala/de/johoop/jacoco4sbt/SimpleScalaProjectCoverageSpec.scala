package de.johoop.jacoco4sbt

import org.specs2.mutable.Specification
import de.johoop.jacoco4sbt.build.BuildInfo
import scala.sys.process.Process
import java.io.File
import org.specs2.matcher.FileMatchers

class SimpleScalaProjectCoverageSpec extends Specification with FileMatchers {

  sequential

  title("JaCoCo in a simple Scala project")

  "Covering tests in a simple Scala project" should {
    "return an exit code != 0 when required coverage is not met" in {
      exitCode !== 0
    }
    "create a jacoco target directory" in {
      jacocoDir should exist and beADirectory
    }
    "create a classes directory" in {
      coveredClassesDir should exist and beADirectory
    }
  }

  lazy val testProjectBase = new File(BuildInfo.test_resourceDirectory, "jacocoTest")
  lazy val targetDir = new File(testProjectBase, "target")
  lazy val jacocoDir = new File(targetDir, "scala-2.10/jacoco")
  lazy val coveredClassesDir = new File(jacocoDir, "classes")

  lazy val exitCode = Process(s"${Util.processName} clean jacoco:cover", Some(testProjectBase)) !
}
