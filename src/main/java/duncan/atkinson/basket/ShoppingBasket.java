package duncan.atkinson.basket;

import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.Product;
import duncan.atkinson.inventory.ProductId;
import duncan.atkinson.inventory.Taxonomy;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

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
     * @return orderLines consisting of the productId and a count of that product.
     */
    public Set<OrderLine> getOrderLines() {
        Map<ProductId, Long> countsByProductId = products.stream()
                .collect(groupingBy(identity(), counting()));
        return countsByProductId
                .entrySet().stream()
                .map(entry -> new OrderLine(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toSet());
    }

    public Set<ProductId> getAllDistinctProductIds() {
        return this.getOrderLines().stream()
                .map(OrderLine::getProductId)
                .collect(toSet());
    }
}
