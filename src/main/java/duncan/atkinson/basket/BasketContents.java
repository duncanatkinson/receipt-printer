package duncan.atkinson.basket;

import duncan.atkinson.Product;

import java.util.Map;

public class BasketContents {

    private final Map<Product, Integer> products;

    public BasketContents(Map<Product, Integer> products) {
        this.products = products;
    }
}
