package duncan.atkinson.basket;

import duncan.atkinson.inventory.ProductId;

import java.util.*;

/**
 * Holds the state of a shopping basket
 */
public class ShoppingBasket {

    /**
     * Product and the count of the number of items of that product
     */
    private List<ProductId> products = new LinkedList<>();

    public void addItem(ProductId productId) {
        products.add(productId);
    }

    public int countItemsInBasket() {
        return products.size();
    }

    public List<ProductId> getContents() {
        return products;
    }
}
