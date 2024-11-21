import Creatures.{ParsedArgs, simpleDateFormatter}
import LogAnalyzer.LogAnalyzer
import Readers.FileSourceFactory
import Writers.AnalyticOutputFactory
import cats.effect.{IO, IOApp}

import java.time.LocalDate
import scala.io.StdIn.readLine
import scala.util.Try

object AnalyzerApp extends IOApp.Simple {

  override def run: IO[Unit] = {
    for {
      /*args <- IO(
        sys.props
          .get("app.args")
          .map(_.split(" "))
          .getOrElse(Array.empty[String])
      )
       */
      paths <- IO {
        readLine("Enter paths: ").split(" ").toSeq
      }
      fromInput <- IO {
        readLine("Enter from: ")
      }
      toInput <- IO {
        readLine("Enter to: ")
      }
      formatInput <- IO {
        val input = readLine(
          "Enter format (markdown/adoc) or press Enter to default (markdown): "
        )
        if (input.trim.isEmpty) "markdown" else input.trim
      }

      // parsed <- parseArgsIO(args)
      parsed <- IO(
        ParsedArgs(
          paths,
          Try(LocalDate.parse(fromInput, simpleDateFormatter)).toOption,
          Try(LocalDate.parse(toInput, simpleDateFormatter)).toOption,
          Some(formatInput)
        )
      )
      logEntries = parsed.paths.flatMap(FileSourceFactory.getSource(_).read())
      analyzer = new LogAnalyzer(logEntries, parsed.from, parsed.to)
      stats = analyzer.calculateStats()
      outputRenderer = AnalyticOutputFactory.create(
        parsed.format.getOrElse("markdown")
      )
      value <- outputRenderer.render(stats, from = parsed.from, to = parsed.to)
      _ <- IO { println(value) }
    } yield ()
  }

  private def parseArgsIO(args: Array[String]): IO[ParsedArgs] = IO {
    val indices = args.zipWithIndex.collect {
      case (key, idx) if key.startsWith("--") => key -> idx
    }.toMap

    val end = indices.values
      .filter(_ > indices.getOrElse("--path", -1))
      .reduceOption(_.min(_))
      .getOrElse(args.length)

    val paths = args.slice(indices("--path") + 1, end)
    val from = indices
      .get("--from")
      .map(x => LocalDate.parse(args(x + 1), simpleDateFormatter))
    val to = indices
      .get("--to")
      .map(x => LocalDate.parse(args(x + 1), simpleDateFormatter))
    val format = indices.get("--format").map(x => args(x + 1))

    ParsedArgs(paths, from, to, format)
  }
}
