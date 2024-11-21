package Creatures

import java.time.LocalDate

case class LogStats(
    totalRequests: Int,
    avgResponseSize: Double,
    responseSize95p: Int,
    resources: Map[String, Int],
    statusCodes: Map[Int, (String, Int)],
    startDate: Option[LocalDate],
    endDate: Option[LocalDate]
)
