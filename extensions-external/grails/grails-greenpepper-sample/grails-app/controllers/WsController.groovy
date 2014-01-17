import groovy.xml.MarkupBuilder
import grails.converters.XML

class WsController {

    def index = { render("REST Services correctly deployed.") }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

    def movie = {

        if (params['id']) {
            def theMovie = Movie.get(params['id'])

            if (!theMovie) {
                response.sendError(404, "Movie does not exist.")
            }

            def writer = new StringWriter()
            def xml = new MarkupBuilder(writer)

            xml.movie(id: theMovie.id)
                    {
                        title(theMovie.title)
                        year(theMovie.year)
                        director(theMovie.director)
                        price(theMovie.price)
                    }

            render(text: writer.toString(), contentType: "text/xml")

        }
        else {
            response.sendError(404, "Please provide a movie Id")
        }
    }

    def list = {
        def all = Movie.list()
        render all as XML
    }
}