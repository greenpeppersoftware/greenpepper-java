class Movie {

    String title
    String director
    int year
    double price

    static constraints = {
        title(blank: false)
        director(blank: false)
        year(min: 1900)
        price(scale: 2, min: 0.01d)
    }
}