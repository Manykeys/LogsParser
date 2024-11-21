package Writers

import Creatures.{LogStats, simpleDateFormatter}
import cats.effect.unsafe.implicits.global
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class MarkdownOutputSpec extends AnyFunSuite with Matchers {

  private val dummyStats = LogStats(
    totalRequests = 150,
    avgResponseSize = 512,
    responseSize95p = 1024,
    resources = Map("/index.html" -> 100, "/about.html" -> 50),
    statusCodes = Map(200 -> ("OK", 140), 404 -> ("Not Found", 10)),
    startDate = null,
    endDate = null
  )

  private val fromDate = Some(LocalDate.of(2024, 11, 1))
  private val toDate = Some(LocalDate.of(2024, 11, 30))

  test("MarkdownOutput should render correctly with from and to dates") {
    val rendered =
      MarkdownOutput.render(dummyStats, fromDate, toDate).unsafeRunSync()

    rendered should include("Количество запросов")
    rendered should include("Средний размер ответа")
    rendered should include("95p размера ответа")
    rendered should include("Начальная дата")
    rendered should include(simpleDateFormatter.format(fromDate.get))
    rendered should include("Конечная дата")
    rendered should include(simpleDateFormatter.format(toDate.get))

    rendered should include("/index.html")
    rendered should include("/about.html")
    rendered should include("| 200 | OK | 140 |")
    rendered should include(" 404 | Not Found | 10 |")
  }

  test("MarkdownOutput should render correctly with no from and to dates") {
    val rendered = MarkdownOutput.render(dummyStats, None, None).unsafeRunSync()

    rendered should include("Количество запросов")
    rendered should include("Средний размер ответа")
    rendered should include("95p размера ответа")
    rendered should include("Начальная дата")
    rendered should include("-")
    rendered should include("Конечная дата")
    rendered should include("-")

    rendered should include("/index.html")
    rendered should include("/about.html")
    rendered should include("| 200 | OK | 140 |")
    rendered should include(" 404 | Not Found | 10 |")
  }
}
