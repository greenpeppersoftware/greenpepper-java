import com.greenpepper.extensions.grails.GrailsSystemUnderDevelopment

class MovieSystemUnderDevelopment extends GrailsSystemUnderDevelopment {

    def MovieSystemUnderDevelopment() {
    }
}

class Money {

    private double dollars

    public Money(double dollars) {
        this.dollars = toCurrency(dollars)
    }

    public double getDollars() {
        return dollars;
    }

    public boolean equals(Object other) {
        if (other instanceof Money) {
            Money that = (Money) other;
            return this.dollars.compareTo(that.dollars) == 0;
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return dollars.hashCode();
    }

    public String toString() {
        return "\$${dollars}"
    }

    public static Money parse(String text) {
        return new Money(Double.parseDouble(normalize(text)))
    }

    private static String normalize(String text) {
        return text.replaceAll(/\$/, "").replaceAll(/,/, "").replaceAll(/\s/, "")
    }

    private static double toCurrency(double dollars) {
        return (double) (Math.round(dollars * 100.0f) / 100.0f);
    }
}

class TaxRate {

    private double rate

    public TaxRate(double rate) {
        this.rate = rate
    }

    public double getRate() {
        return rate;
    }

    public static TaxRate parse(String text) {
        return new TaxRate(Double.parseDouble(normalize(text)))
    }

    private static String normalize(String text) {
        return text.replaceAll(/%/, "").replaceAll(/,/, "").replaceAll(/\s/, "")
    }
}

class MovieStoreFixture {

    def movieService

    def boolean thatServiceIsNotNull() {
        return movieService != null
    }

    def resetMovieList() {
        Movie.list()*.delete()
        return true
    }

    def boolean createNewMovieFromYearWithDirectorAtAPriceOf(String title, int year, String director, Money price) {
        def movie = new Movie(title: title, year: year, director: director, price: price.dollars)
        if (!movie.validate()) {
            movie.errors.allErrors.each { println it.code }
            throw new Exception("Validation failed!")
        }
        return movie.save(flush: true)
    }

    def thatPriceOfMovieIsEqualsTo(String title) {
        def movie = findMovieByTitle(title)
        return new Money(movie.price)
    }

    def thatPriceOfMovieWithTaxRateOfIsEqualsTo(String title, TaxRate taxRate) {
        def movie = findMovieByTitle(title)
        return new Money(movieService.computePriceWithTax(movie.price, taxRate.getRate()))
    }

    def movieList() {
        return Movie.list()
    }

    private Movie findMovieByTitle(String title) {
        def movie = Movie.findByTitle(title)
        if (!movie) throw new Exception("Cannot find movie with title '${title}'!")
        return movie
    }
}