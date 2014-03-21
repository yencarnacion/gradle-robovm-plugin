//https://github.com/bmuschko/gradle-tomcat-plugin/blob/master/plugin/src/main/groovy/org/gradle/api/plugins/tomcat/TomcatPlugin.groovy
package com.webninjapr.api.plugins.robovm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.robovm.compiler.AppCompiler
import org.robovm.compiler.config.Config
import org.robovm.compiler.config.Config.TargetType
import org.robovm.compiler.config.OS
import org.robovm.compiler.config.Arch

import org.robovm.compiler.log.Logger
import org.robovm.compiler.target.ios.IOSSimulatorLaunchParameters
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * <p>A {@link Plugin} to add tasks which compile a Java application using the
 * robovm compiler.</p>
 *
 * @author Yamir Encarnacion
 */
class RobovmPlugin implements Plugin<Project> {
    Logger slf4jLogger = LoggerFactory.getLogger('some-logger')

    @Override
    void apply(Project project) {



        // Add the 'greeting' extension object
        project.extensions.create("robovm", RobovmPluginExtension)
        // Add a task that uses the configuration
        project.task('compileRobovm') << {

            if (project.robovm.robovmLogger == null) {
                project.robovm.robovmLogger = new org.robovm.compiler.log.Logger() {
                    public void debug(String s, Object... objects) {
                        slf4jLogger.debug(String.format(s, objects));
                    }

                    public void info(String s, Object... objects) {
                        slf4jLogger.info(String.format(s, objects));
                    }

                    public void warn(String s, Object... objects) {
                        slf4jLogger.warn(String.format(s, objects));
                    }

                    public void error(String s, Object... objects) {
                        slf4jLogger.error(String.format(s, objects));
                    }
                };
            }
            def builder = new Config.Builder()


            builder.mainClass(project.robovm.mainClass)
                    .executableName(project.robovm.executableName)
                    .logger(project.robovm.robovmLogger)
                    .skipInstall(project.robovm.skipInstall)
                    .targetType(project.robovm.targetType)
                    .os(project.robovm.os)
                    .arch(project.robovm.arch)



            project.robovm.classpath.each({
                if(it.exists() && it.canRead()) {
                    println "cpe: " + it.getPath();
                    builder.addClasspathEntry(it)
                } else {
                    println "cpe (Skipping): " + it.getPath();
                }
            })

            project.robovm.robovmLogger?.info("Compiling RoboVM app, this could take a while")
            println "Compiling RoboVM app!"
            def config = builder.build()
            def compiler = new AppCompiler(config)
            compiler.compile()

            println("Launching RoboVM app")

            IOSSimulatorLaunchParameters launchParameters = config.getTarget().createLaunchParameters()
            launchParameters.setFamily(IOSSimulatorLaunchParameters.Family.iPhoneRetina4Inch)
            config.getTarget().launch(launchParameters).waitFor()

        }
    }


}



class RobovmPluginExtension {

    def mainClass = null
    def classpath = null
    def executableName = "RoboVM App"


    def robovmLogger = null;

    def skipInstall = false
    def targetType = TargetType.ios
    def os = OS.ios
    def arch = Arch.x86 // ipadSimTask || iphoneSimTask

    String filePath

    def installDir = new File("./build")
    def tmpDir = new File("./build/native")


}