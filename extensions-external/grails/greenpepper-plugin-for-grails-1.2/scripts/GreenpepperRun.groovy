import org.codehaus.groovy.grails.commons.GrailsApplication
import com.greenpepper.Statistics
import com.greenpepper.extensions.grails.GrailsCliRunner
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.orm.hibernate3.SessionHolder
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

    compileFixtures()
    packageFixtures();
    loadApp()
    configureApp()
    configureHibernateSession()

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

def configureHibernateSession() {
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = SessionFactoryUtils.getSession(sessionFactory, true)
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
}

compileFixtures = {
    event("CompileStart", ['greenpepper-fixtures'])

    def destDir = grailsSettings.classesDir
    ant.mkdir(dir: destDir.path)

    try {
        def classpathId = "grails.compile.classpath"
        ant.groovyc(destdir: destDir,
                projectName: grailsAppName,
                encoding:"UTF-8",
                classpathref: classpathId) {
            javac(classpathref:classpathId, debug:"yes")
            src(path:"${basedir}/test/greenpepper")
        }
    }
    catch (Exception e) {
        event("StatusFinal", ["Compilation Error: ${e.message}"])
        exit(1)
    }

    event("CompileEnd", ['greenpepper-fixtures'])
}

packageFixtures = {
    packageApp()

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

