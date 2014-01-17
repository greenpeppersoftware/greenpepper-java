class MovieController {

    def index = { redirect(action: list, params: params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

    def search = {

        def cList = Movie.createCriteria()

        def searchClosure = {

            if (params.qTitle) {
                ilike("title", "%${params.qTitle}%")
            }

            if (params.qDirector) {
                ilike("director", "%${params.qDirector}%")
            }
        }

        def movies = cList.list(searchClosure)

        render(view: 'list', model: [movieInstanceList: movies, qTitle: "${params.qTitle}", qDirector: "${params.qDirector}"])
    }

    def list = {
        params.max = Math.min( params.max?.toInteger() ?: 10, 100)
        [movieInstanceList: Movie.list(params)]
    }

    def show = {
        [movieInstance: Movie.get(params.id)]
    }

    def delete = {
        def movie = Movie.get(params.id)
        if (movie) {
            movie.delete()
            flash.message = "Movie ${params.id} deleted"
            redirect(action: list)
        }
        else {
            flash.message = "Movie not found with id ${params.id}"
            redirect(action: list)
        }
    }

    def edit = {
        def movie = Movie.get(params.id)

        if (!movie) {
            flash.message = "Movie not found with id ${params.id}"
            redirect(action: list)
        }
        else {
            return [movieInstance: movie]
        }
    }

    def update = {
        def movie = Movie.get(params.id)
        if (movie) {
            movie.properties = params
            if (!movie.hasErrors() && movie.save()) {
                flash.message = "Movie ${params.id} updated"
                redirect(action: show, id: movie.id)
            }
            else {
                render(view: 'edit', model: [movieInstance: movie])
            }
        }
        else {
            flash.message = "Movie not found with id ${params.id}"
            redirect(action: edit, id: params.id)
        }
    }

    def create = {
        def movie = new Movie()
        movie.properties = params
        return ['movie': movie]
    }

    def save = {
        def movie = new Movie(params)
        if (!movie.hasErrors() && movie.save()) {
            flash.message = "Movie ${movie.id} created"
            redirect(action: show, id: movie.id)
        }
        else {
            render(view: 'create', model: [movieInstance: movie])
        }
    }
}