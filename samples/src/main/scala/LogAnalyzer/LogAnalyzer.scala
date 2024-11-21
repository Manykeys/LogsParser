package LogAnalyzer

import Creatures.{LogEntry, LogStats, statusCodeNames}

import java.time.LocalDate

class LogAnalyzer(
    logEntries: Seq[LogEntry],
    from: Option[LocalDate],
    to: Option[LocalDate]
) {

  def calculateStats(): LogStats = {
    val filteredLogs = logEntries.filter { entry =>
      val date = entry.timestamp.map(_.toLocalDate)
      date.exists { d =>
        val fromCheck = from match {
          case Some(fromDate) => !d.isBefore(fromDate)
          case _              => true
        }

        val toCheck = to match {
          case Some(toDate) => !d.isAfter(toDate)
          case _            => true
        }

        fromCheck && toCheck
      }
    }

    val totalRequests = filteredLogs.size

    val responseSizes = filteredLogs.map(_.bodyBytesSent)
    val avgResponseSize =
      if (responseSizes.nonEmpty) responseSizes.sum.toDouble / totalRequests
      else 0.0
    val responseSize95p = if (responseSizes.nonEmpty) {
      val sortedSizes = responseSizes.sorted
      sortedSizes((sortedSizes.size * 0.95).toInt)
    } else 0

    val resources = filteredLogs
      .map(_.request.split(" ").lift(1).getOrElse("-"))
      .groupBy(identity)
      .map((x, y) => (x, y.length))

    val statusCodes = filteredLogs
      .groupBy(_.status)
      .map { case (code, entries) =>
        code -> (statusCodeNames.getOrElse(code, "Unknown"), entries.size)
      }

    val dates = filteredLogs.flatMap(_.timestamp.map(_.toLocalDate))
    val startDate = dates.minOption
    val endDate = dates.maxOption

    LogStats(
      totalRequests,
      avgResponseSize,
      responseSize95p,
      resources,
      statusCodes,
      startDate,
      endDate
    )
  }
}
