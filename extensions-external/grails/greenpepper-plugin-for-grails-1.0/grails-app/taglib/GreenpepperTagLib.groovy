class GreenpepperTagLib {

    static namespace = "gp"

    def link = {attrs ->
        def map = grailsApplication.metadata
        String appName = map["app.name"]
        out << "<a href=\"/${appName}/greenPepper\" style=\"background: url(/${appName}${pluginContextPath}/images/greenpepper/greenpepper_16.png) center left no-repeat;color: #333;padding-left: 25px;\">GreenPepper</a>"
    }

    def ifTestEnv = {attrs, body ->
        if (grailsApplication.ENV_TEST == getEnv()) {
            out << body()
        }
    }

    def ifDevOrTestEnv = {attrs, body ->
        String env = getEnv()
        if (grailsApplication.ENV_DEVELOPMENT == env || grailsApplication.ENV_TEST == env) {
            out << body()
        }
    }

    private String getEnv() {
        def map = grailsApplication.metadata
        return map[grailsApplication.ENVIRONMENT]
    }
}