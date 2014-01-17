class MovieTests extends GroovyTestCase {

    void testSomething() {
        def m1 = new Movie(title: "Die Hard", director: "John", price: 10.00).save()
        def m2 = new Movie(title: "Die Hard 2", director: "John", price: 10.00).save()

        def mc = new MovieController()
        mc.params['qTitle'] = "Die Hard"
        mc.search()

        println(mc.modelAndView.model.movieList)
    }
}