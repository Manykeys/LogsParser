package Writers

import Creatures.{LogStats, simpleDateFormatter}
import cats.effect.IO

import java.time.LocalDate

object AdocOutput extends AnalyticOutput {
  def render(
      stats: LogStats,
      from: Option[LocalDate],
      to: Option[LocalDate]
  ): IO[String] = {

    val fromDate = from.map(simpleDateFormatter.format).getOrElse("-")
    val toDate = to.map(simpleDateFormatter.format).getOrElse("-")

    val output =
      s"""
         |== Общая информация
         |
         |[cols="20,20", options="header"]
         ||===
         || Метрика                | Значение
         || Количество запросов    | ${stats.totalRequests}
         || Средний размер ответа  | ${stats.avgResponseSize}b
         || 95p размера ответа     | ${stats.responseSize95p}b
         || Начальная дата         | $fromDate
         || Конечная дата          | $toDate
         ||===
         |
         |== Запрашиваемые ресурсы
         |
         |[cols="20,20", options="header"]
         ||===
         || Ресурс                | Количество
         |${stats.resources
          .map { case (res, count) => s"| $res | $count" }
          .mkString("\n")}
         ||===
         |
         |== Коды ответа
         |
         |[cols="10,30,20", options="header"]
         ||===
         || Код | Имя                     | Количество
         |${stats.statusCodes
          .map { case (code, (name, count)) => s"| $code | $name | $count" }
          .mkString("\n")}
         ||===
         |""".stripMargin
    IO(output)
  }
}
