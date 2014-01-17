class BootStrap {

    def init = {servletContext ->

        Movie.list()*.delete(flush: true)

        new Movie(title: "Superman Returns", year: 2006, director: "Bryan Signer", price: 19.99f).save()
        new Movie(title: "Spiderman", year: 2002, director: "Sam Raimi", price: 15.99f).save()
        new Movie(title: "Spiderman 2", year: 2004, director: "Sam Raimi", price: 15.99f).save()
        new Movie(title: "X-Men Origins: Wolverine", year: 2009, director: "Gavin Hood", price: 35.99f).save()
        new Movie(title: "Fight Club", year: 1999, director: "David Fincher", price: "8.99").save()
        new Movie(title: "Iron Man", year: 2008, director: "Jon Favreau", price: 21.50f).save()
    }

    def destroy = {
    }
}
