package Writers

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class AnalyticOutputSpec extends AnyFunSuite with Matchers {

  test(
    "AnalyticOutputFactory should create MarkdownOutput when format is 'markdown'"
  ) {
    val output = AnalyticOutputFactory.create("markdown")
    output shouldBe a[MarkdownOutput.type]
  }

  test("AnalyticOutputFactory should create AdocOutput when format is 'adoc'") {
    val output = AnalyticOutputFactory.create("adoc")
    output shouldBe a[AdocOutput.type]
  }

  test("AnalyticOutputFactory should throw an exception for unknown formats") {
    val exception = intercept[IllegalArgumentException] {
      AnalyticOutputFactory.create("unknown")
    }
    exception.getMessage should include("Unknown format")
  }
}
