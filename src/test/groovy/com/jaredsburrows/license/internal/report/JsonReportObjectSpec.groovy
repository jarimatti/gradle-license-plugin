package com.jaredsburrows.license.internal.report

import com.jaredsburrows.license.internal.Developer
import com.jaredsburrows.license.internal.License
import com.jaredsburrows.license.internal.report.json.JsonReportObject
import spock.lang.Specification

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
final class JsonReportObjectSpec extends Specification {
  def developers = [Developer.builder().name("name").build()]
  def licenses = [License.builder().name("name").url("url").build()]
  def sut = JsonReportObject.builder().name("name").developers(developers).url("url").year("year").licenses(licenses).build()

  def "test get name"() {
    expect:
    sut.name == "name"
    sut.getName() == "name"
  }

  def "test developers"() {
    expect:
    sut.developers == developers
    sut.getDevelopers() == developers
  }

  def "test url"() {
    expect:
    sut.url == "url"
    sut.getUrl() == "url"
  }

  def "test year"() {
    expect:
    sut.year == "year"
    sut.getYear() == "year"
  }

  def "test licenses"() {
    expect:
    sut.licenses == licenses
    sut.getLicenses() == licenses
  }

  def "test equals/hashcode"() {
    given:
    def one = JsonReportObject.builder().name("name").developers(developers).url("url").year("year").licenses(licenses).build()
    def two = JsonReportObject.builder().name("name").developers(developers).url("url").year("year").licenses(licenses).build()

    expect:
    // Values
    one.name == two.name
    one.developers == two.developers
    one.url == two.url
    one.year == two.year
    one.licenses == two.licenses
    // Auto generated
    one.hashCode() == two.hashCode()
    // one == two
    one.toString() == two.toString()
  }
}
