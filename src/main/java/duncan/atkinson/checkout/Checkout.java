package duncan.atkinson.checkout;

import duncan.atkinson.basket.OrderLine;
import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.Product;
import duncan.atkinson.inventory.ProductId;
import duncan.atkinson.inventory.Taxonomy;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.disjoint;

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
        return basket.getOrderLines().stream()
                .map(productAndCount -> calculateCostOfOrderLine(productAndCount, basket))
                .mapToInt(Integer::intValue)
                .sum();
    }

    private void checkMaxPurchaseLimitsNotHit(ShoppingBasket basket) throws CheckoutException {
        basket.getOrderLines().forEach(orderLine -> {
            Product product = inventory.get(orderLine.getProductId());
            Integer maximumPurchaseVolume = product.getMaximumPurchaseVolume();

            if (maximumPurchaseVolume != null && orderLine.getCount() > maximumPurchaseVolume) {
                throw new CheckoutException(product.getProductId().name() + " purchase limit is 10");
            }
        });
    }

    public int calculateTax(ShoppingBasket basket) {
        Set<OrderLine> orderLines = basket.getOrderLines();
        int taxableSum = orderLines.stream()
                .filter(taxApplies())
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
    private int calculateCostOfOrderLine(OrderLine orderLine, ShoppingBasket basket) {
        ProductId productId = orderLine.getProductId();
        Product product = inventory.get(productId);

        int numberOfItems = adjustCountIfBOGOF(orderLine);
        int priceInCents = product.getPriceInCents() * numberOfItems;

        priceInCents = applyTaxonomyDiscounts(basket, product, priceInCents);
        return priceInCents;
    }

    private int applyTaxonomyDiscounts(ShoppingBasket basket, Product product, int priceInCents) {
        if (product.hasTaxonomyDiscount()) {
            Taxonomy taxonomyTriggeringDiscount = product.getTaxonomyDiscount().getTaxonomy();
            Set<ProductId> productIdsInBasket = basket.getAllDistinctProductIds();
            Set<ProductId> productIdsMatchingTaxonomy = inventory.getProductIdsWithTaxonomy(taxonomyTriggeringDiscount);
            if (isIntersection(productIdsMatchingTaxonomy, productIdsInBasket)) {
                priceInCents = (priceInCents / 100) * (100 - product.getTaxonomyDiscount().getPercentageDiscount());
            }
        }
        return priceInCents;
    }

    private boolean isIntersection(Set<ProductId> set1, Set<ProductId> set2) {
        return !disjoint(set2, set1);
    }

    private int adjustCountIfBOGOF(OrderLine orderLine) {
        if (inventory.get(orderLine.getProductId()).getBuyOneGetOneFree()) {
            int bogofCount = orderLine.getCount() / 2;
            bogofCount += orderLine.getCount() % 2;
            return bogofCount;
        }
        return orderLine.getCount();
    }


    private Predicate<OrderLine> taxApplies() {
        return orderLine -> !inventory.get(orderLine.getProductId()).getTaxExempt();
    }
}
