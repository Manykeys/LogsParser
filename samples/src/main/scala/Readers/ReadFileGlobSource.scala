package Readers

import Creatures.{LogEntry, parseLogLine}

import java.nio.file.{FileSystems, Files, Path, Paths}

case class ReadFileGlobSource(globPattern: String) extends FileSource {
  override def read(): Seq[LogEntry] = {
    val files = findFiles(globPattern)
    files.flatMap { file =>
      val lines = Files.readAllLines(file).toArray
      lines.flatMap(line => parseLogLine(line.toString))
    }
  }

  private def findFiles(globPattern: String): List[Path] = {
    val pathMatcher =
      FileSystems.getDefault.getPathMatcher(s"glob:$globPattern")
    walkFiles(Paths.get("."), pathMatcher, List.empty)
  }

  private def walkFiles(
      dir: Path,
      pathMatcher: java.nio.file.PathMatcher,
      accumulator: List[Path]
  ): List[Path] = {
    val result = Files
      .walk(dir)
      .toArray
      .collect {
        case path: Path if pathMatcher.matches(path) => path
      }
      .toList
    accumulator ++ result
  }
}
