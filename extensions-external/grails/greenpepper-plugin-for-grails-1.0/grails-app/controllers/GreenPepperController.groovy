import com.greenpepper.extensions.grails.GrailsCliRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class GreenPepperController implements ApplicationContextAware {

    def applicationContext

    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    def index = {

        def runnerBeanNames = applicationContext.getBeanNamesForType(GrailsCliRunner)

        render(view: 'index', model: [runners: runnerBeanNames])
    }
}