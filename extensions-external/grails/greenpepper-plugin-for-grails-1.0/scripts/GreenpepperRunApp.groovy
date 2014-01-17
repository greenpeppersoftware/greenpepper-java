/**
 * Gant script that runs the application with GreenPepper
 *
 * @since 0.1
 */
scriptEnv = "test"
grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << grailsScript ( "Init" )
includeTargets << grailsScript ( "Clean" )
includeTargets << grailsScript ( "RunApp" )

target('default': "Run application with GreenPepper fixtures") {
    depends(classpath, checkVersion, parseArguments, clean, cleanTestReports, configureProxy)

    rootLoader.addURL(classesDir.toURL())

    compileFixtures()
    packageFixtures();

    runApp()
    watchContext()
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