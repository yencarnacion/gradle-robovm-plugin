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

            //builder.home(new Config.Home(new File((String) project.robovm.filePath)))


            //builder.installDir(project.robovm.installDir)
            //builder.tmpDir(project.robovm.tmpDir)

            /*println "classpath: " +project.robovm.classpath
            project.robovm.classpath.each(
                    {
                        println "h: "+ ((File) it.ge)

                    }

            )*/

            project.robovm.classpath.each({
                if(it.exists() && it.canRead()) {
                    println "cpe: " + it.getPath();
                    builder.addClasspathEntry(it)
                } else {
                    println "cpe (Skipping): " + it.getPath();
                }
            })

//            b.fullClasspath.map(i => i.data) foreach { file =>
//                st.log.debug("Including classpath item: " + file)
//                builder.addClasspathEntry(file)
//            }
            project.robovm.robovmLogger?.info("Compiling RoboVM app, this could take a while")
            println "Compiling RoboVM app!"
            def config = builder.build()
            def compiler = new AppCompiler(config)
            compiler.compile()

            println("Launching RoboVM app")

            IOSSimulatorLaunchParameters launchParameters = config.getTarget().createLaunchParameters()
            launchParameters.setFamily(IOSSimulatorLaunchParameters.Family.iphone)
            config.getTarget().launch(launchParameters).waitFor()

            /*

            b.propertiesFile map { file =>
                st.log.debug("Including properties file: " + file.getAbsolutePath())
                builder.addProperties(file)
            }

            b.configFile map { file =>
                st.log.debug("Loading config file: " + file.getAbsolutePath())
                builder.read(file)
            }

            b.forceLinkClasses foreach { pattern =>
                st.log.debug("Including class pattern: " + pattern)
                builder.addForceLinkClass(pattern)
            }

            b.frameworks foreach { framework =>
                st.log.debug("Including framework: " + framework)
                builder.addFramework(framework)
            }

            for (dir <- b.nativePath if dir.isDirectory) {
                dir.listFiles foreach { lib =>
                    st.log.debug("Including lib: " + lib)
                    builder.addLib(lib.getPath())
                }
            }

            b.fullClasspath.map(i => i.data) foreach { file =>
                st.log.debug("Including classpath item: " + file)
                builder.addClasspathEntry(file)
            }

            b.unmanagedResources foreach { file =>
                st.log.debug("Including resource: " + file)
                val resource = new Resource(file)
                        .skipPngCrush(b.skipPngCrush)
                        .flatten(b.flattenResources)
                builder.addResource(resource)
            }

            ios.sdkVersion map { version =>
                st.log.debug("Using explicit iOS SDK version: " + version)
                builder.iosSdkVersion(version)
            }

            ios.signIdentity map { identity =>
                st.log.debug("Using explicit iOS Signing identity: " + identity)
                builder.iosSignIdentity(SigningIdentity.find(SigningIdentity.list(), identity))
            }

            ios.provisioningProfile map { profile =>
                st.log.debug("Using explicit iOS provisioning profile: " + profile)
                builder.iosProvisioningProfile(ProvisioningProfile.find(ProvisioningProfile.list(), profile))
            }

            ios.infoPlist map { file =>
                st.log.debug("Using Info.plist file: " + file.getAbsolutePath())
                builder.iosInfoPList(file)
            }

            ios.entitlementsPlist map { file =>
                st.log.debug("Using Entitlements.plist file: " + file.getAbsolutePath())
                builder.iosEntitlementsPList(file)
            }

            ios.resourceRulesPlist map { file =>
                st.log.debug("Using ResourceRules.plist file: " + file.getAbsolutePath())
                builder.iosResourceRulesPList(file)
            }

            builder.installDir(t)
            builder.tmpDir(t / "native")

            st.log.info("Compiling RoboVM app, this could take a while")
            val config = builder.build()
            val compiler = new AppCompiler(config)
            compiler.compile()

            st.log.info("Launching RoboVM app")
            launcher(config)*/
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

    /*def String  forceLinkClasses
    def String  frameworks
    def String  nativePath
    def Boolean skipPngCrush = false
    def Boolean flattenResources = false
    def String  propertiesFile
    def String  configFile
    def String  distHome
    def String  iosSdkVersion
    def String  iosSignIdentity
    def String  iosProvisioningProfile
    def String  iosInfoPlist
    def String  iosEntitlementsPlist
    def String  iosResourceRulesPlist*/
}