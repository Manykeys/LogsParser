package org.backend.academy.loganalyzer

import Creatures.*
import LogAnalyzer.LogAnalyzer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class LogAnalyzerSpec extends AnyFlatSpec with Matchers:
  private val logEntries = Seq(
    LogEntry(
      "127.0.0.1",
      "-",
      "01/Jan/2023:00:00:00 +0000",
      "GET /index.html HTTP/1.1",
      200,
      1234,
      "-",
      "Mozilla/5.0"
    ),
    LogEntry(
      "127.0.0.2",
      "-",
      "02/Jan/2023:00:00:00 +0000",
      "POST /api/data HTTP/1.1",
      404,
      567,
      "-",
      "Mozilla/5.0"
    )
  )

  "LogAnalyzer" should "calculate correct stats for filtered logs" in {
    val analyzer = new LogAnalyzer(
      logEntries,
      from = Some(LocalDate.parse("2023-01-01")),
      to = Some(LocalDate.parse("2023-01-02"))
    )
    val stats = analyzer.calculateStats()

    stats.totalRequests shouldBe 2
    stats.avgResponseSize shouldBe (1234 + 567).toDouble / 2
    stats.responseSize95p shouldBe 1234
    stats.resources should contain("/index.html" -> 1)
    stats.resources should contain("/api/data" -> 1)
    stats.statusCodes should contain(200 -> ("OK", 1))
    stats.statusCodes should contain(404 -> ("Not Found", 1))
    stats.startDate shouldBe Some(LocalDate.parse("2023-01-01"))
    stats.endDate shouldBe Some(LocalDate.parse("2023-01-02"))
  }

  it should "return zero stats for an empty log" in {
    val analyzer = LogAnalyzer(Seq.empty, None, None)
    val stats = analyzer.calculateStats()

    stats.totalRequests shouldBe 0
    stats.avgResponseSize shouldBe 0.0
    stats.responseSize95p shouldBe 0
    stats.resources shouldBe empty
    stats.statusCodes shouldBe empty
    stats.startDate shouldBe None
    stats.endDate shouldBe None
  }
end LogAnalyzerSpec
