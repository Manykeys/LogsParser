package Creatures

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class LogEntry(
    remoteAddr: String,
    remoteUser: String,
    timeLocal: String,
    request: String,
    status: Int,
    bodyBytesSent: Int,
    httpReferer: String,
    httpUserAgent: String
) {
  lazy val timestamp: Option[LocalDateTime] = {
    val formatter = DateTimeFormatter.ofPattern(
      "dd/MMM/yyyy:HH:mm:ss Z",
      java.util.Locale.ENGLISH
    )
    val zonedDateTime = java.time.ZonedDateTime.parse(timeLocal, formatter)
    Some(zonedDateTime.toLocalDateTime)
  }
}

val logPattern =
  """^([\d\.]+) - (.*?) \[([\w:/]+\s[+\-]\d{4})\] "(.+?)" (\d{3}) (\d+) "(.*?)" "(.*?)"$""".r

def parseLogLine(line: String): Option[LogEntry] = {
  line match {
    case logPattern(
          remoteAddr,
          remoteUser,
          timeLocal,
          request,
          status,
          bodyBytesSent,
          httpReferer,
          httpUserAgent
        ) =>
      Some(
        LogEntry(
          remoteAddr,
          remoteUser,
          timeLocal,
          request,
          status.toInt,
          bodyBytesSent.toInt,
          httpReferer,
          httpUserAgent
        )
      )
    case _ => None
  }
}
