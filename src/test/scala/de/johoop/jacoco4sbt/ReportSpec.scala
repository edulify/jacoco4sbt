/*
 * This file is part of jacoco4sbt.
 *
 * Copyright (c) 2014 Joachim Hofer & contributors
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.johoop.jacoco4sbt

import org.mockito.Mockito._
import sbt.Keys._
import org.jacoco.core.analysis.{IBundleCoverage, ICounter}
import org.specs2.mutable.{Before, Specification}
import sbt.Logger

class ReportSpec extends Specification with Before {

  sequential

  title("Report Unit Test")

  def before = {
    val mockLog = mock(classOf[Logger])
    when(mockStreams.log).thenReturn(mockLog)
    when(mockBundle.getLineCounter).thenReturn(mockICounter)
    when(mockBundle.getInstructionCounter).thenReturn(mockICounter)
    when(mockBundle.getBranchCounter).thenReturn(mockICounter)
    when(mockBundle.getMethodCounter).thenReturn(mockICounter)
    when(mockBundle.getComplexityCounter).thenReturn(mockICounter)
    when(mockBundle.getClassCounter).thenReturn(mockICounter)
  }

  "Report.checkCounter" should {

    "return false if coverage is less than required" in {
      when(mockICounter.getMissedCount).thenReturn(30)
      when(mockICounter.getTotalCount).thenReturn(40)
      when(mockICounter.getCoveredRatio).thenReturn(0.25)
      report.checkCounter("foo", mockICounter, 50) mustEqual false
    }
    "return true if coverage is more than required" in {
      when(mockICounter.getMissedCount).thenReturn(10)
      when(mockICounter.getTotalCount).thenReturn(40)
      when(mockICounter.getCoveredRatio).thenReturn(0.75)
      report.checkCounter("foo", mockICounter, 50) mustEqual true
    }
    "return true if coverage is equals ro required" in {
      when(mockICounter.getMissedCount).thenReturn(20)
      when(mockICounter.getTotalCount).thenReturn(40)
      when(mockICounter.getCoveredRatio).thenReturn(0.50)
      report.checkCounter("foo", mockICounter, 50) mustEqual true
    }
    "return true if required coverage is 0 and ratio is NaN" in {
      when(mockICounter.getMissedCount).thenReturn(0)
      when(mockICounter.getTotalCount).thenReturn(0)
      when(mockICounter.getCoveredRatio).thenReturn(Double.NaN)
      report.checkCounter("foo", mockICounter, 0) mustEqual true
    }
    "return true if required coverage is 0 and ratio is 0" in {
      when(mockICounter.getMissedCount).thenReturn(40)
      when(mockICounter.getTotalCount).thenReturn(0)
      when(mockICounter.getCoveredRatio).thenReturn(0)
      report.checkCounter("foo", mockICounter, 0) mustEqual true
    }
  }

  "Report.checkCoverage" should {
    "return true if all required coverage metrics are met" in {
      when(mockICounter.getMissedCount).thenReturn(10)
      when(mockICounter.getTotalCount).thenReturn(40)
      when(mockICounter.getCoveredRatio).thenReturn(0.75)
      report.checkCoverage(mockBundle) mustEqual true
    }
    "return false if at least one metric is not met" in {
      when(mockICounter.getMissedCount).thenReturn(10).thenReturn(30)
      when(mockICounter.getTotalCount).thenReturn(40)
      when(mockICounter.getCoveredRatio).thenReturn(0.75).thenReturn(0.25)
      report.checkCoverage(mockBundle) mustEqual false
    }
  }

  lazy val mockStreams = mock(classOf[TaskStreams])
  lazy val mockICounter = mock(classOf[ICounter])
  lazy val mockBundle = mock(classOf[IBundleCoverage])
  lazy val report = new Report(null, null, null, null, 0, null, null, null,
    Thresholds(instruction = 35, method = 40, branch = 30, complexity = 35, line = 50, clazz = 40),
    mockStreams)
}
