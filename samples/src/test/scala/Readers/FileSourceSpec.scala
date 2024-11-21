package Readers

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class FileSourceSpec extends AnyFunSuite with Matchers {

  test("FileSourceFactory should return ReadFileUrlSource for URL paths") {
    val urlPath = "https://example.com/logs/access.log"
    val source = FileSourceFactory.getSource(urlPath)

    source shouldBe a[ReadFileUrlSource]
    source.asInstanceOf[ReadFileUrlSource].url shouldEqual urlPath
  }

  test(
    "FileSourceFactory should return ReadFileGlobSource for local file paths"
  ) {
    val localPath = "/var/logs/*.log"
    val source = FileSourceFactory.getSource(localPath)

    source shouldBe a[ReadFileGlobSource]
  }

  test("FileSourceFactory should correctly identify URLs") {
    val validUrl = "http://example.com/logs"
    val anotherUrl = "https://example.org"
    val invalidPath = "/local/path/to/logs"

    FileSourceFactory.isUrl(validUrl) shouldBe true
    FileSourceFactory.isUrl(anotherUrl) shouldBe true
    FileSourceFactory.isUrl(invalidPath) shouldBe false
  }
}
