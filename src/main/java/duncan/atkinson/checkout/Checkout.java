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

import static java.util.Collections.disjoint;
import static java.util.stream.Collectors.toList;

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
                .map(productAndCount -> {
                    CostResult costResult = calculateCost(productAndCount, basket);
                    return costResult.costInCents.minus(costResult.discountAmount);
                })
                .reduce(CHF::add)
                .orElse(new CHF(0));
    }

    public CHF calculateTax(List<ReceiptLine> receiptLines) {
        return receiptLines.stream()
                .map(ReceiptLine::getTax)
                .reduce(CHF::add)
                .orElse(new CHF(0));
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
     * <p>
     * There is a potential issue here that if a bogof and a special offer both apply
     * then the description will be just special offer.
     *
     * @param orderLine is a map entry consisting of the product ID and the count of that product.
     * @param basket    used for discounts
     * @return the cost of those products.
     */
    private CostResult calculateCost(OrderLine orderLine, ShoppingBasket basket) {
        ProductId productId = orderLine.getProductId();
        Product product = inventory.get(productId);
        int beforeDiscountCost = product.getPriceInCents() * orderLine.getCount();
        int numberOfItems = orderLine.getCount();
        String discountDescription = null;
        if (inventory.get(orderLine.getProductId()).getBuyOneGetOneFree() && orderLine.getCount() > 1) {
            numberOfItems = adjustCountForBOGOF(orderLine);
            discountDescription = "BOGOF";
        }
        int costAdjustedForBogof = product.getPriceInCents() * numberOfItems;

        int finalCost = adjustCostForTaxonomyDiscounts(basket, product, costAdjustedForBogof);

        if (costAdjustedForBogof != finalCost) {
            discountDescription = "SPECIAL";
        }

        CHF costInWholeCurrencyUnits = new CHF(beforeDiscountCost).divide(100);
        CHF discountInWholeCurrencyUnits = new CHF(beforeDiscountCost - finalCost).divide(100);
        return new CostResult(costInWholeCurrencyUnits, discountInWholeCurrencyUnits, discountDescription);
    }

    /**
     * Just a small private internal class to hold the cost of an {@link OrderLine}
     */
    private static class CostResult {
        private CHF costInCents;
        private CHF discountAmount;
        private String discountDescription;

        public CostResult(CHF costInCents, CHF discountAmount, String discountDescription) {
            this.costInCents = costInCents;
            this.discountAmount = discountAmount;
            this.discountDescription = discountDescription;
        }


    }

    private int adjustCostForTaxonomyDiscounts(ShoppingBasket basket, Product product, int priceInCents) {
        if (product.hasTaxonomyDiscount()) {
            Taxonomy taxonomyTriggeringDiscount = product.getTaxonomyDiscount().getTaxonomy();
            List<ProductId> productIdsInBasket = basket.getAllDistinctProductIds();
            List<ProductId> productIdsMatchingTaxonomy = inventory.getProductIdsWithTaxonomy(taxonomyTriggeringDiscount);
            if (isIntersection(productIdsMatchingTaxonomy, productIdsInBasket)) {
                priceInCents = (priceInCents / 100) * (100 - product.getTaxonomyDiscount().getPercentageDiscount());
            }
        }
        return priceInCents;
    }

    private boolean isIntersection(List<ProductId> set1, List<ProductId> set2) {
        return !disjoint(set2, set1);
    }

    private int adjustCountForBOGOF(OrderLine orderLine) {
        int bogofCount = orderLine.getCount() / 2;
        bogofCount += orderLine.getCount() % 2;
        return bogofCount;
    }


    private boolean taxApplies(OrderLine orderLine) {
        return !inventory.get(orderLine.getProductId()).getTaxExempt();
    }

    public Receipt checkout(ShoppingBasket basket) throws CheckoutException {
        checkMaxPurchaseLimitsNotHit(basket);
        List<ReceiptLine> lines = basket.getOrderLines().stream()
                .map(orderLine -> calculate(orderLine, basket))
                .collect(toList());
        CHF taxAmount = calculateTax(lines);
        CHF totalCost = calculateCost(basket);
        return new Receipt(lines, CHF.add(totalCost, taxAmount), taxAmount);
    }

    private ReceiptLine calculate(OrderLine orderLine, ShoppingBasket basket) {
        CostResult costResult = calculateCost(orderLine, basket);
        CHF tax = new CHF(0);
        if (taxApplies(orderLine)) {
            CHF actualCost = costResult.costInCents.minus(costResult.discountAmount);
            tax = calculateTax(actualCost);
        }
        String productDescription = inventory.get(orderLine.getProductId()).getName();
        if (orderLine.getCount() > 1) {
            productDescription += " * " + orderLine.getCount();
        }
        if (costResult.discountAmount.equals(new CHF(0))) {
            return new ReceiptLine(productDescription, costResult.costInCents, tax);
        } else {
            return new ReceiptLine(productDescription, costResult.costInCents, tax, costResult.discountAmount, costResult.discountDescription);
        }
    }
}
