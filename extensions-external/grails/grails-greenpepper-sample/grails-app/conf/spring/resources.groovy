import com.greenpepper.extensions.grails.GrailsCliRunner
import com.greenpepper.runner.repository.GreenPepperRepository
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

// Place your Spring DSL code here
beans = {

    switch (GrailsUtil.environment) {
        case GrailsApplication.ENV_TEST:

            def reportsDir = "test/reports/greenpepper"

            movieSystemUnderDevelopment(MovieSystemUnderDevelopment)

            grailsExampleRepository(GreenPepperRepository, "http://www.greenpeppersoftware.com/confluence/rpc/xmlrpc?handler=greenpepper1&sut=Extensions - GRAILS&includeStyle=true#GPO")

            RemoteSuite(GrailsCliRunner) {
                suite = true
                input = "GreenPepper-GPO"
                outputType = "plain" // plain or xml
                output = "${reportsDir}"
                failOnError = false
                debug = true
                systemUnderDevelopment = ref("movieSystemUnderDevelopment")
                documentRepository = ref("grailsExampleRepository")
            }

            LocalSuite(GrailsCliRunner) {
                suite = true
                input = "test/greenpepper"
                outputType = "plain" // plain or xml
                output = "${reportsDir}"
                failOnError = false
                debug = true
                systemUnderDevelopment = ref("movieSystemUnderDevelopment")
            }

            MovieStoreSpecification(GrailsCliRunner) {
                input = "test/greenpepper/MovieStore.html"
                outputType = "plain" // plain or xml
                output = "${reportsDir}/MovieStore-executed.html"
                failOnError = true
                debug = true
                // Using default SUD found in the plugin (GrailsSystemUnderDevelopment)
                //systemUnderDevelopment = ref("movieSystemUnderDevelopment")
            }

            movieStoreFixture(MovieStoreFixture) {
                movieService = ref("movieService")
            }

        break
    }
}