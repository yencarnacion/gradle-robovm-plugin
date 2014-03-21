gradle-robovm-plugin
================

gradle-robovm-plugin is copyrighted free software by Yamir Encarnación <yencarnacion@webninjapr.com>.
You can redistribute it and/or modify it under the terms of the GPLv3
(see the file NOTICE.txt).

gradle-robovm-plugin is a gradle plugin that aims to make it simpler to compile Scala (and Java) using [RoboVM](http://www.robovm.org/) (version 0.0.10)

## Notes
Inspired by the [sbt-robovm plugin](https://github.com/ajhager/sbt-robovm)

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

See [robovm-ios-samples](https://github.com/yencarnacion/robovm-ios-samples) to see the plugin in action
