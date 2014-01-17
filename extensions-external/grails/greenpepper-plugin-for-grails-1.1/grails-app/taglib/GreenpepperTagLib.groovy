import grails.util.Environment

class GreenpepperTagLib {

    static namespace = "gp"

    def link = {attrs ->
        def map = grailsApplication.metadata
        String appName = map["app.name"]
        out << "<a href=\"/${appName}/greenPepper\" style=\"background: url(/${appName}${pluginContextPath}/images/greenpepper/greenpepper_16.png) center left no-repeat;color: #333;padding-left: 25px;\">GreenPepper</a>"
    }

    def ifTestEnv = {attrs, body ->
        if (Environment.TEST == Environment.current) {
            out << body()
        }
    }

    def ifDevOrTestEnv = {attrs, body ->
        if (Environment.DEVELOPMENT== Environment.current || Environment.TEST == Environment.current) {
            out << body()
        }
    }
}