Note: to avoid confusion with the [gradle-robovm-plugin](https://github.com/jtakakura/gradle-robovm-plugin) the name of the plugin was changed to gradle-wn-robovm-plugin.  You can find the gradle-wn-robovm-plugin repository at [https://github.com/yencarnacion/gradle-wn-robovm-plugin](https://github.com/yencarnacion/gradle-wn-robovm-plugin).  This repository will remain as to serve as a breadcrumb for the new repository but will not longer be actively developed.  New development will occur in [https://github.com/yencarnacion/gradle-wn-robovm-plugin](https://github.com/yencarnacion/gradle-wn-robovm-plugin).

gradle-robovm-plugin
================

gradle-robovm-plugin is copyrighted free software by Yamir Encarnación <yencarnacion@webninjapr.com>.
You can redistribute it and/or modify it under the terms of the GPLv3
(see the file NOTICE.txt).

gradle-robovm-plugin is a gradle plugin that aims to make it simpler to compile Scala and Java using [RoboVM](http://www.robovm.org/) (version 0.0.10)

## Notes
Inspired by the [sbt-robovm plugin](https://github.com/ajhager/sbt-robovm)

## Setup

1. Install Xcode (tested with 5.1)
2. Install [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
3. Download and extract [robovm-0.0.10.tar.gz](http://download.robovm.org/robovm-0.0.10.tar.gz) to one of these places:
 * $ROBOVM_HOME
 * ~/Applications/robovm/
 * ~/.robovm/home/
 * /usr/local/lib/robovm/
 * /opt/robovm/
 * /usr/lib/robovm/

## Usage

Include the following in your build.gradle:

    buildscript {
        repositories {
            mavenCentral()
            maven {
                url uri('http://dl.bintray.com/yencarnacion/webninjapr-maven')
            }
        }
        dependencies {
            classpath 'org.robovm:robovm-compiler:0.0.10'
            classpath group: 'com.webninjapr', name: 'gradle-robovm-plugin', version: '0.0.1'
        }
    }

And include an extension that looks as follows:

    robovm {
        mainClass= 'HelloJava'
        classpath = runtimeClasspath
    }

Replace mainClass with your main class.

See [robovm-ios-samples](https://github.com/yencarnacion/robovm-ios-samples) to see the plugin in action
