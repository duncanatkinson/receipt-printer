package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.inventory.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * ø
 * Calculates the cost of a shopping basket
 * TODO return a Currency object
 */
public class Checkout {

    private static final Integer TAX_RATE = 12;
    private final Inventory inventory;

    public Checkout(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * @param basket to calculate
     * @return the total cost of the contents of the basket in cents applying all discounts but without the tax
     */
    public int calculateTotalBeforeTax(ShoppingBasket basket) throws CheckoutException {
        checkMaxPurchaseLimitsNotHit(basket);
        return basket.getOrderLines().entrySet().stream()
                .map(productAndCount -> calculateCostOfOrderLine(productAndCount, basket))
                .mapToInt(Integer::intValue)
                .sum();
    }

    private void checkMaxPurchaseLimitsNotHit(ShoppingBasket basket) throws CheckoutException{
        basket.getOrderLines().forEach((productId, itemCount) -> {
            Product product = inventory.get(productId);
            Integer maximumPurchaseVolume = product.getMaximumPurchaseVolume();
            if (maximumPurchaseVolume != null && itemCount > maximumPurchaseVolume) {
                throw new CheckoutException(product.getProductId().name() + " purchase limit is 10");
            }
        }); }

    public int calculateTax(ShoppingBasket basket) {
        Map<ProductId, Long> orderLines = basket.getOrderLines();
        int taxableSum = orderLines.entrySet().stream()
                .filter(entry -> !taxExempt(entry.getKey()))
                .map(productAndCount -> calculateCostOfOrderLine(productAndCount, basket))
                .mapToInt(Integer::intValue)
                .sum();
        return (taxableSum / 100) * TAX_RATE;
    }

    /**
     * Calculate the cost of a number of products in the basket
     * note this isn't just a simple sum, because it is possible discounts apply.
     *
     * @param orderLine is a map entry consisting of the product ID and the count of that product.
     * @param basket    used for discounts
     * @return the cost of those products.
     */
    private int calculateCostOfOrderLine(Map.Entry<ProductId, Long> orderLine, ShoppingBasket basket) {
        ProductId productId = orderLine.getKey();
        Product product = inventory.get(productId);

        // adjustPriceFor BOGOF
        int numberOfItems = orderLine.getValue().intValue();
        numberOfItems = adjustCountIfBOGOF(orderLine, numberOfItems);
        Integer priceInCents = product.getPriceInCents();
        priceInCents = priceInCents * numberOfItems;

        // apply taxonomy discounts
        if (product.hasTaxonomyDiscount() && taxonomyDiscountApplies(product.getTaxonomyDiscount(), basket)) {
            priceInCents = (priceInCents / 100) * (100 - product.getTaxonomyDiscount().getPercentageDiscount());
        }
        return priceInCents;
    }

    private boolean taxonomyDiscountApplies(TaxonomyDiscount taxonomyDiscount, ShoppingBasket basket) {
        Set<Taxonomy> taxonomyInBasket = basket.getOrderLines().keySet().stream()
                .map(inventory::get)
                .map(Product::getTaxonomy)
                .flatMap(Collection::stream)
                .collect(toSet());
        return taxonomyInBasket.contains(taxonomyDiscount.getTaxonomy());
    }

    private int adjustCountIfBOGOF(Map.Entry<ProductId, Long> productAndCount, int count) {
        if (inventory.get(productAndCount.getKey()).getBuyOneGetOneFree()) {
            count = count / 2;
            count += productAndCount.getValue().intValue() % 2;
        }
        return count;
    }

    private boolean taxExempt(ProductId productId) {
        return inventory.get(productId).getTaxExempt();
    }
}
