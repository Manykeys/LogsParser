package Readers

import Creatures.{LogEntry, parseLogLine}

import scala.io.Source

case class ReadFileUrlSource(url: String) extends FileSource {
  override def read(): Seq[LogEntry] = {
    val source = Source.fromURL(url)
    val lines = source.getLines().toSeq
    sys.addShutdownHook {
      source.close()
    }
    lines.flatMap(parseLogLine)
  }
}
