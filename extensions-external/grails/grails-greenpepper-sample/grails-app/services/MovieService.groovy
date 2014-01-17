class MovieService {

    double computePriceWithTax(double price, double rate) {
        return price + ((price * rate) / 100)
    }
}