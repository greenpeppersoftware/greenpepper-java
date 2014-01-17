import com.greenpepper.runner.SpecificationRunnerMonitor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class GreenPepperRunnerController implements ApplicationContextAware {

    def applicationContext

    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    def run = {
        def runner = applicationContext.getBean(params.id)

        def buffer = new StringBuilder("<br/>")

        def monitor = [
                testRunning: {String location ->
                    buffer.append("Running ${location}<br/>")
                    println "Running ${location}"
                },
                testDone: {int r, int w, int e, int i ->
                    buffer.append("${r + w + e + i} tests: ${r} right, ${w} wrong, ${i} ignore, ${e} exception<br/>")
                    println "${r + w + e + i} tests: ${r} right, ${w} wrong, ${i} ignore, ${e} exception"
                },
                exceptionOccured: {Throwable t ->
                    buffer.append("exceptionOccured ${t.printStackTrace()}<br/>")
                    println "exceptionOccured ${t.printStackTrace()}"
                }
        ] as SpecificationRunnerMonitor

        SpecificationRunnerMonitor previousMonitor = runner.monitor;
        runner.monitor = monitor

        try {
            runner.run()
        }
        catch (Exception ex) {
            buffer.append("Exception:").append(ex.toString());
        }
        finally {
            runner.monitor = previousMonitor
        }

        render "Runned : ${buffer.toString()}"
    }
}