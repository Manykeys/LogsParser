package Creatures

import java.time.LocalDate

case class ParsedArgs(
    paths: Seq[String],
    from: Option[LocalDate],
    to: Option[LocalDate],
    format: Option[String]
)
