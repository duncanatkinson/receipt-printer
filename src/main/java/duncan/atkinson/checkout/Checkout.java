package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.ProductId;

/**
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
    public int calculateTotalBeforeTax(ShoppingBasket basket) {
        return basket.getContents().stream()
                .map(productId -> inventory.get(productId).getPriceInCents())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int calculateTax(ShoppingBasket basket) {
        int taxableSum = basket.getContents().stream()
                .filter(productId -> !taxExempt(productId))
                .map(productId -> inventory.get(productId).getPriceInCents())
                .mapToInt(Integer::intValue)
                .sum();
        return (taxableSum/100) * TAX_RATE;
    }

    private boolean taxExempt(ProductId productId) {
        return inventory.get(productId).getTaxExempt();
    }
}
