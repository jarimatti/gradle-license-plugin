package com.jaredsburrows.license.internal.report

import com.jaredsburrows.license.internal.Developer
import com.jaredsburrows.license.internal.License
import com.jaredsburrows.license.internal.Project
import com.jaredsburrows.license.internal.report.json.JsonReport
import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
final class JsonReportSpec extends Specification {
  def licenses = [License.builder().name("name").url("url").build()]
  def developers = [Developer.builder().name("name").build()]
  def projects = [Project.builder().name("name").licenses(licenses).url("url").developers(developers).year("year").build()]
  def sut = new JsonReport(projects)

  def "test toJson"() {
    given:
    def json = sut.toJson()
    def parse = new JsonSlurper().parseText(json)

    expect:
    println parse
    parse[0]["project"] == "name"
    parse[0]["authors"] == "name"
    parse[0]["url"] == "url"
    parse[0]["year"] == "year"
    parse[0]["license"] == "name"
    parse[0]["license_url"] == "url"
  }
}
