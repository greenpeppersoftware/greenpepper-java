import com.greenpepper.extensions.grails.GrailsSystemUnderDevelopment

class GreenpepperGrailsPlugin {
    def version = 0.1
    def dependsOn = [:]

    def author = "fdenommee, clapointe"
    def authorEmail = "fdenommee@pyxis-tech.com, clapointe@pyxis-tech.com"
    def title = "GreenPepper Plugin"
    def description = '''\
Provides various GreenPepper functionnalities and more.
'''

    // URL to the plugin's documentation
    def documentation = "http://www.greenpeppersoftware.com/confluence/display/GPWODOC/Grails+Plugin"

    def doWithSpring = {
        systemUnderDevelopment(GrailsSystemUnderDevelopment)
    }
   
    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)		
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }
	                                      
    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }
	
    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
