package com.jaredsburrows.license

import groovy.json.JsonSlurper
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
final class LicenseReportTaskSpec extends Specification {
  final static def COMPILE_SDK_VERSION = 25
  final static def BUILD_TOOLS_VERSION = "25.0.2"
  final static def APPLICATION_ID = "com.example"
  final static def SUPPORT_VERSION = "25.1.0"
  // Maven repo - "file://${System.env.ANDROID_HOME}/extras/android/m2repository"
  final static def APPCOMPAT_V7 = "com.android.support:appcompat-v7:${SUPPORT_VERSION}"
  final static def DESIGN = "com.android.support:design:${SUPPORT_VERSION}"
  final static def SUPPORT_ANNOTATIONS = "com.android.support:support-annotations:${SUPPORT_VERSION}"
  final static def SUPPORT_V4 = "com.android.support:support-v4:${SUPPORT_VERSION}"
  // Maven repo - "file://${System.env.ANDROID_HOME}/extras/google/m2repository"
  final static def FIREBASE_CORE = "com.google.firebase:firebase-core:10.0.1"
  // Test fixture that emulates a mavenCentral()/jcenter()/"https://plugins.gradle.org/m2/"
  final static def TEST_MAVEN_REPOSITORY = getClass().getResource("/maven/").toURI()
  def project
  def assertDir
  def htmlFile
  def jsonFile

  def "setup"() {
    // Common project
    project = ProjectBuilder.builder().build()
    project.repositories {
      maven { url TEST_MAVEN_REPOSITORY }
    }

    // Override output directories
    assertDir = File.createTempDir()
//    assertDir.deleteOnExit()
    htmlFile = new File(assertDir.path, "test.html")
    jsonFile = new File(assertDir.path, "test.json")
  }

  def "test java licenseReport - build.gradle with no dependencies"() {
    given:
    project.apply plugin: "java"
    project.dependencies {}

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReport")
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "No open source libraries"
    // Nothing else
    !html.text().contains("Notice for libraries:")
  }

  def "test android licenseDebugReport - build.gradle with no dependencies"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }
    }
    project.dependencies {}

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseDebugReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "No open source libraries"
    // Nothing else
    !html.text().contains("Notice for libraries:")
  }

  def "test android licenseReleaseReport - build.gradle with no dependencies"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }
    }
    project.dependencies {}

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReleaseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "No open source libraries"
    // Nothing else
    !html.text().contains("Notice for libraries:")
  }

  def "test java licenseReport - build.gradle with no open source dependencies"() {
    given:
    project.apply plugin: "java"
    project.dependencies {
      delegate.compile(FIREBASE_CORE)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReport")
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "No open source libraries"
    // Nothing else
    !html.text().contains("Notice for libraries:")
  }

  def "test android licenseDebugReport - build.gradle with no open source dependencies"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }
    }
    project.dependencies {
      delegate.compile(FIREBASE_CORE)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseDebugReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "No open source libraries"
    // Nothing else
    !html.text().contains("Notice for libraries:")
  }

  def "test android licenseReleaseReport - build.gradle with no open source dependencies"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }
    }
    project.dependencies {
      delegate.compile(FIREBASE_CORE)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReleaseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "No open source libraries"
    // Nothing else
    !html.text().contains("Notice for libraries:")
  }

  def "test java licenseReport - default"() {
    given:
    project.apply plugin: "java"
    project.dependencies {
      // Handles duplicates
      delegate.compile(APPCOMPAT_V7)
      delegate.compile(APPCOMPAT_V7)
      delegate.compile(DESIGN)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReport")
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[2]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[2]
  }

  def "test android licenseDebugReport - default buildTypes"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }
    }
    project.dependencies {
      // Handles duplicates
      delegate.compile(APPCOMPAT_V7)
      delegate.compile(APPCOMPAT_V7)
      delegate.compile(DESIGN)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseDebugReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[2]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[2]
  }

  def "test android licenseReleaseReport - default buildTypes"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }
    }
    project.dependencies {
      // Handles duplicates
      delegate.compile(APPCOMPAT_V7)
      delegate.compile(APPCOMPAT_V7)
      delegate.compile(DESIGN)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReleaseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[2]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[2]
  }

  def "test android licenseDebugReport - default and debug buildTypes"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }
    }
    project.dependencies {
      delegate.compile(APPCOMPAT_V7)
      delegate.debugCompile(DESIGN)
      delegate.releaseCompile(SUPPORT_ANNOTATIONS)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseDebugReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[2]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[2]
  }

  def "test android licenseReleaseReport - default and debug buildTypes"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }
    }
    project.dependencies {
      delegate.compile(APPCOMPAT_V7)
      delegate.debugCompile(SUPPORT_ANNOTATIONS)
      delegate.releaseCompile(DESIGN)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReleaseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[2]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[2]
  }

  def "test android licenseFlavor1DebugReport - default, debug buildTypes and productFlavors"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }

      productFlavors {
        flavor1 {}
        flavor2 {}
      }
    }
    project.dependencies {
      delegate.compile(APPCOMPAT_V7)
      delegate.debugCompile(DESIGN)
      delegate.flavor1Compile(SUPPORT_V4)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseFlavor1DebugReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.ul.li[2].text() == "Support-v4"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[3]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[2].project == "Support-v4"
    !json[2].authors
    !json[2].url
    !json[2].year
    json[2].license == "The Apache Software License"
    json[2].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[3]
  }

  def "test android licenseFlavor2ReleaseReport - default, debug buildTypes and productFlavors"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }

      productFlavors {
        flavor1 {}
        flavor2 {}
      }
    }
    project.dependencies {
      delegate.compile(APPCOMPAT_V7)
      delegate.releaseCompile(DESIGN)
      delegate.flavor2Compile(SUPPORT_V4)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseFlavor2ReleaseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.ul.li[2].text() == "Support-v4"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[3]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[2].project == "Support-v4"
    !json[2].authors
    !json[2].url
    !json[2].year
    json[2].license == "The Apache Software License"
    json[2].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[3]
  }

  def "test android licenseFlavor1Flavor3DebugReport - default, debug buildTypes and productFlavors dimensions"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }

      flavorDimensions "a", "b"

      productFlavors {
        flavor1 { dimension "a" }
        flavor2 { dimension "a" }
        flavor3 { dimension "b" }
        flavor4 { dimension "b" }
      }
    }
    project.dependencies {
      delegate.compile(APPCOMPAT_V7)
      delegate.debugCompile(DESIGN)
      delegate.flavor1Compile(SUPPORT_V4)
      delegate.flavor3Compile(SUPPORT_ANNOTATIONS)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseFlavor1Flavor3DebugReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.ul.li[2].text() == "Support-annotations"
    html.body.ul.li[3].text() == "Support-v4"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[4]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[2].project == "Support-annotations"
    !json[2].authors
    !json[2].url
    !json[2].year
    json[2].license == "The Apache Software License"
    json[2].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[3].project == "Support-v4"
    !json[3].authors
    !json[3].url
    !json[3].year
    json[3].license == "The Apache Software License"
    json[3].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[4]
  }

  def "test android licenseFlavor2Flavor4ReleaseReport - default, debug buildTypes and productFlavors dimensions"() {
    given:
    project.apply plugin: "com.android.application"
    project.android {
      compileSdkVersion COMPILE_SDK_VERSION
      buildToolsVersion BUILD_TOOLS_VERSION

      defaultConfig {
        applicationId APPLICATION_ID
      }

      buildTypes {
        debug {}
        release {}
      }

      flavorDimensions "a", "b"

      productFlavors {
        flavor1 { dimension "a" }
        flavor2 { dimension "a" }
        flavor3 { dimension "b" }
        flavor4 { dimension "b" }
      }
    }
    project.dependencies {
      delegate.compile(APPCOMPAT_V7)
      delegate.releaseCompile(DESIGN)
      delegate.flavor2Compile(SUPPORT_V4)
      delegate.flavor4Compile(SUPPORT_ANNOTATIONS)
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseFlavor2Flavor4ReleaseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Appcompat-v7"
    html.body.ul.li[1].text() == "Design"
    html.body.ul.li[2].text() == "Support-annotations"
    html.body.ul.li[3].text() == "Support-v4"
    html.body.pre[0].text() == "The Apache Software License, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[4]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Appcompat-v7"
    !json[0].authors
    !json[0].url
    !json[0].year
    json[0].license == "The Apache Software License"
    json[0].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[1].project == "Design"
    !json[1].authors
    !json[1].url
    !json[1].year
    json[1].license == "The Apache Software License"
    json[1].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[2].project == "Support-annotations"
    !json[2].authors
    !json[2].url
    !json[2].year
    json[2].license == "The Apache Software License"
    json[2].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    json[3].project == "Support-v4"
    !json[3].authors
    !json[3].url
    !json[3].year
    json[3].license == "The Apache Software License"
    json[3].license_url == "http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[4]
  }

  def "test java licenseReport - single developers, multiple licenses"() {
    given:
    project.repositories {
      maven { url "https://plugins.gradle.org/m2/" }
    }
    project.apply plugin: "java"
    project.dependencies {
      delegate.compile("com.github.bumptech.glide:glide:3.7.0")
    }

    when:
    project.evaluate()
    new LicensePlugin().apply(project)

    // Change output directory for testing
    def task = project.tasks.getByName("licenseReport")
    task.assetDirs = [assertDir]
    task.htmlFile = htmlFile
    task.jsonFile = jsonFile
    task.execute()

    then:
    def html = new XmlParser().parse(task.htmlFile)
    // Title
    html.head.title.text() == "Open source licenses"
    html.body.h3[0].text() == "Notice for libraries:"
    // Dependencies
    html.body.ul.li[0].text() == "Glide"
    html.body.pre[0].text() == "Simplified BSD License, The Apache Software License, Version 2.0, http://www.opensource.org/licenses/bsd-license, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !html.body.ul.li[1]
    !html.body.pre[1]

    def json = new JsonSlurper().parse(task.jsonFile)
    // Dependencies
    json[0].project == "Glide"
    json[0].authors == "Sam Judd"
    json[0].url == "https://github.com/bumptech/glide"
    !json[0].year
    json[0].license == "Simplified BSD License, The Apache Software License, Version 2.0"
    json[0].license_url == "http://www.opensource.org/licenses/bsd-license, http://www.apache.org/licenses/LICENSE-2.0.txt"
    // Nothing else
    !json[1]
  }
}
