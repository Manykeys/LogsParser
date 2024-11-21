package Writers

import Creatures.LogStats
import cats.effect.IO

import java.time.LocalDate

trait AnalyticOutput {
  def render(
      stats: LogStats,
      from: Option[LocalDate],
      to: Option[LocalDate]
  ): IO[String]
}

object AnalyticOutputFactory {
  def create(format: String): AnalyticOutput = format.toLowerCase match {
    case "markdown" => MarkdownOutput
    case "adoc"     => AdocOutput
    case _ => throw new IllegalArgumentException(s"Unknown format: $format")
  }
}
