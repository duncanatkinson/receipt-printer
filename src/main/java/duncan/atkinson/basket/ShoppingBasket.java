package duncan.atkinson.basket;

import duncan.atkinson.dataobjects.OrderLine;
import duncan.atkinson.inventory.ProductId;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    public List<OrderLine> getOrderLines() {
        Map<ProductId, Long> countsByProductId = products.stream()
                .collect(groupingBy(identity(), counting()));
        return countsByProductId
                .entrySet().stream()
                .map(entry -> new OrderLine(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public List<ProductId> getAllDistinctProductIds() {
        return this.getOrderLines().stream()
                .map(OrderLine::getProductId)
                .collect(toList());
    }
}
