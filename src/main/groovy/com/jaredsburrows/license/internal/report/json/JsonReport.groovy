package com.jaredsburrows.license.internal.report.json

import com.jaredsburrows.license.internal.Project
import groovy.json.JsonOutput

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
final class JsonReport {
  final List<Project> projects
  final jsonArray = []

  JsonReport(projects) {
    this.projects = projects
  }

  def jsonArray() {
    // Create new license object for each project
    projects.each { project ->
      final def object = JsonReportObject.builder()
        .name(project.name)
        .developers(project.developers)
        .url(project.url)
        .year(project.year)
        .licenses(project.licenses)
        .build()
        .jsonObject()

      jsonArray.add(object)
    }

    jsonArray
  }

  def toJson() {
    JsonOutput.toJson(jsonArray())
  }
}
