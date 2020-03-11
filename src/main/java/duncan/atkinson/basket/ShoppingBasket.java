package duncan.atkinson.basket;

import duncan.atkinson.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingBasket {

    Map<Product, Integer> products = new HashMap<>();

    public void addItem(Product product) {
        products.put(product, 1);
    }

    public Set<Product> getProductsInBasket() {
        return products.keySet();
    }

    public boolean hasProduct(Product product) {
        return getProductsInBasket().contains(product);
    }
}
