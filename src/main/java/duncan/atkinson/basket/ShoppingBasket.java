package duncan.atkinson.basket;

import duncan.atkinson.inventory.ProductId;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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

    /**
     * TODO refactor from Map.Entry into OrderLines
     *
     * @return orderLines consisting of the productId and a count of that product.
     */
    public Map<ProductId, Long> getOrderLines() {
        return products.stream()
                .collect(groupingBy(identity(), counting()));
    }
}
