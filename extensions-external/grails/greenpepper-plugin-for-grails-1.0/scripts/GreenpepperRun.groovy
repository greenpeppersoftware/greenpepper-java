import org.codehaus.groovy.grails.commons.GrailsApplication
import com.greenpepper.Statistics
import com.greenpepper.extensions.grails.GrailsCliRunner
/**
 * Gant script that runs the Green Pepper specifications
 *
 * @since 0.1
 */
scriptEnv = "test"
grailsHome = Ant.project.properties."environment.GRAILS_HOME"
grailsApp = null
appCtx = null

includeTargets << grailsScript ( "Bootstrap" )
includeTargets << grailsScript ( "Clean" )
includeTargets << grailsScript ( "Package" )

target('default': "Run the Green Pepper tests") {
    depends(classpath, checkVersion, parseArguments, clean, cleanTestReports, configureProxy)

    rootLoader.addURL(classesDir.toURL())

    compileFixtures()
    packageFixtures();

    loadApp()
    configureApp()

    event("StatusUpdate", ["Executing specifications"])

    try {
        def start = new Date()

        def app = appCtx.getBean(GrailsApplication.APPLICATION_ID)
        if (app.parentContext == null) {
            app.applicationContext = appCtx
        }

        def runnerBeanNames = appCtx.getBeanNamesForType(GrailsCliRunner)
        if (runnerBeanNames.length == 0) throw new Exception("No runner found!")

        Statistics statistics = new Statistics();

        runnerBeanNames.each {name ->
            event("StatusUpdate", ["\nExecuting specifications for runner '${name}'"])
            def cliRunner = appCtx.getBean(name);
            assert cliRunner != null: "Grails CLI Runner is null!";
            cliRunner.run()
            statistics.tally cliRunner.statistics
        }

        def end = new Date()
        event("StatusFinal", ["Green Pepper specifications completed in [${end.time - start.time}ms]"])
        event("StatusFinal", ["Green Pepper Summary : ${statistics.toString()}"])
    }
    catch (Exception ex) {
        ex.printStackTrace()
        event("StatusFinal", ["Execution Error: ${ex.message}"])
        exit(1)
    }
}

target(compileFixtures: "Compiles GreenPepper fixtures") {

    event("CompileStart", ['greenpepper-fixtures'])

    def destDir = testDirPath
    Ant.mkdir(dir: destDir)
    try {
        def fixtureClasspath = compilerClasspath.curry(true)
        Ant.groovyc(destdir: destDir,
                projectName: grailsAppName,
                encoding: "UTF-8",
                resourcePattern:"file:${basedir}/**/grails-app/**/*.groovy",
                classpathref: "grails.classpath", {
                    fixtureClasspath.delegate = delegate
                    fixtureClasspath.call()
                    src(path:"${basedir}/test/greenpepper")
                })
    }
    catch (Exception e) {
        event("StatusFinal", ["Compilation Error: ${e.message}"])
        exit(1)
    }

	classLoader = new URLClassLoader([new File((String)destDir).toURI().toURL()] as URL[], getClass().classLoader.rootLoader)
	Thread.currentThread().contextClassLoader = classLoader
    
    event("CompileEnd", ['greenpepper-fixtures'])
}

target(packageFixtures:"Add fixtures stuff on the classpath") {
    depends(packageApp)

	Ant.copy(todir:testDirPath, failonerror:false) {
        fileset(dir:"${basedir}/test/greenpepper") {
            include(name:"**/**")
            exclude(name:"**/*.html")
            exclude(name:"**/*.confluence")
            exclude(name:"**/*.java")
            exclude(name:"**/*.groovy)")
        }
	}
}