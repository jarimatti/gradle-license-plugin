package com.jaredsburrows.license.internal.report.json

import com.jaredsburrows.license.internal.Developer
import com.jaredsburrows.license.internal.License
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
@Builder(excludes = "jsonObject")
@EqualsAndHashCode(includeFields = true, useCanEqual = false)
@ToString(includeNames = true, includePackage = false)
final class JsonReportObject {
  final static def PROJECT = "project"
  final static def DEVELOPERS = "authors"
  final static def URL = "url"
  final static def YEAR = "year"
  final static def LICENSE = "license"
  final static def LICENSE_URL = "license_url"
  def jsonObject = [:]
  String name
  List<Developer> developers
  String url
  String year
  List<License> licenses

  /**
   * Convert object to a JsonObject.
   */
  def jsonObject() {
    // Project name
    jsonObject.put(PROJECT, name)

    // Authors/developers
    if (developers) jsonObject.put(DEVELOPERS, developers?.collect { developer -> developer?.name }?.join(", "))

    // Project url
    if (url) jsonObject.put(URL, url)

    // Inception year
    if (year) jsonObject.put(YEAR, year)

    // Project license
    if (licenses) {
      jsonObject.put(LICENSE, licenses?.collect { license -> license?.name }?.join(", "))
      jsonObject.put(LICENSE_URL, licenses?.collect { license -> license?.url }?.join(", "))
    }

    jsonObject
  }
}
