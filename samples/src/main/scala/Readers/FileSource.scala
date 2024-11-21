package Readers

import Creatures.LogEntry

import java.net.URI
import scala.util.Try

trait FileSource {
  def read(): Seq[LogEntry]
}

object FileSourceFactory {
  def getSource(path: String): FileSource = {
    if (isUrl(path)) ReadFileUrlSource(path)
    else ReadFileGlobSource(path)
  }

  def isUrl(path: String): Boolean = Try(
    new URI(path).toURL
  ).isSuccess
}
