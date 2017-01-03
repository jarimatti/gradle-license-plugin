package com.jaredsburrows.license.internal

import spock.lang.Specification

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
final class DevelopersSpec extends Specification {
  def sut = Developer.builder().name("name").build()

  def "test get name"() {
    expect:
    sut.name == "name"
    sut.getName() == "name"
  }

  def "test equals/hashcode"() {
    given:
    def one = Developer.builder().name("name").build()
    def two = Developer.builder().name("name").build()

    expect:
    // Values
    one.name == two.name
    // Auto generated
    one.hashCode() == two.hashCode()
    // one == two
    one.toString() == two.toString()
  }
}
