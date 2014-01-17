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

    compileFixtures()
    packageFixtures();

    runApp()
    watchContext()
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
