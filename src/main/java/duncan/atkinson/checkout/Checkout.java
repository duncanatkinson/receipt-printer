package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.dataobjects.CHF;
import duncan.atkinson.dataobjects.OrderLine;
import duncan.atkinson.dataobjects.Receipt;
import duncan.atkinson.dataobjects.ReceiptLine;
import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.Product;
import duncan.atkinson.inventory.ProductId;
import duncan.atkinson.inventory.Taxonomy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.disjoint;

/**
 * Calculates the cost of a shopping basket
 * TODO return a Currency object
 */
public class Checkout {

    private static final String TAX_RATE = "12.0";
    private final Inventory inventory;

    public Checkout(Inventory inventory) {
        this.inventory = inventory;
    }


    /**
     * @param basket to calculate
     * @return the total cost of the contents of the basket in cents applying all discounts but without the tax
     */
    protected CHF calculateCost(ShoppingBasket basket) throws CheckoutException {
        checkMaxPurchaseLimitsNotHit(basket);
        return basket.getOrderLines().stream()
                .map(productAndCount -> calculateCost(productAndCount, basket).costInCents)
                .reduce(CHF::add)
                .orElse(new CHF(0));
    }

    public CHF calculateTax(ShoppingBasket basket) {
        Set<OrderLine> orderLines = basket.getOrderLines();
        CHF taxableSum = orderLines.stream()
                .filter(this::taxApplies)
                .map(orderLine -> calculateCost(orderLine, basket))
                .map(costResult -> costResult.costInCents)
                .reduce(CHF::add)
                .orElse(new CHF(0));
        return calculateTax(taxableSum);
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

    /**
     * @param taxableSum
     * @return tax payable on sum
     */
    private CHF calculateTax(CHF taxableSum) {
        CHF tax = taxableSum.multiply(TAX_RATE);
        return tax.divide(100);
    }

    /**
     * Calculate the cost of a number of products in the basket
     * note this isn't just a simple sum, because it is possible discounts apply.
     *
     * @param orderLine is a map entry consisting of the product ID and the count of that product.
     * @param basket    used for discounts
     * @return the cost of those products.
     */
    private CostResult calculateCost(OrderLine orderLine, ShoppingBasket basket) {
        ProductId productId = orderLine.getProductId();
        Product product = inventory.get(productId);
        int beforeDiscountCost = product.getPriceInCents() * orderLine.getCount();
        int numberOfItems = adjustCountIfBOGOF(orderLine);
        int actualCostInCents = product.getPriceInCents() * numberOfItems;

        actualCostInCents = applyTaxonomyDiscounts(basket, product, actualCostInCents);
        CHF costInWholeCurrencyUnits = new CHF(actualCostInCents).divide(100);
        CHF discountInWholeCurrencyUnits = new CHF(beforeDiscountCost - actualCostInCents).divide(100);
        return new CostResult(costInWholeCurrencyUnits, discountInWholeCurrencyUnits);
    }

    /**
     * Just a small private internal class to hold the cost of an {@link OrderLine}
     */
    private static class CostResult {
        private CHF costInCents;
        private CHF discountAmount;

        public CostResult(CHF costInCents, CHF discountAmount) {
            this.costInCents = costInCents;
            this.discountAmount = discountAmount;
        }
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


    private boolean taxApplies(OrderLine orderLine) {
        return !inventory.get(orderLine.getProductId()).getTaxExempt();
    }

    public Receipt checkout(ShoppingBasket basket) {
        checkMaxPurchaseLimitsNotHit(basket);
        List<ReceiptLine> lines = basket.getOrderLines().stream()
                .map(orderLine -> calculate(orderLine, basket))
                .collect(Collectors.toList());
        return new Receipt(lines, calculateCost(basket), calculateTax(basket));
    }

    private ReceiptLine calculate(OrderLine orderLine, ShoppingBasket basket) {
        CostResult costResult = calculateCost(orderLine, basket);
        CHF tax = new CHF(0);
        if (taxApplies(orderLine)) {
            tax = calculateTax(costResult.costInCents);
        }
        String productDescription = inventory.get(orderLine.getProductId()).getName();
        if (orderLine.getCount() > 1) {
            productDescription += " * " + orderLine.getCount();
        }
        return new ReceiptLine(productDescription, costResult.costInCents, tax, costResult.discountAmount);
    }
}
