package duncan.atkinson.checkout;

import java.math.BigDecimal;
import java.util.Objects;

public class ReceiptLine {
    private final String productDescription;
    private final int cost;
    private final BigDecimal tax;
    private final int discountAmount;

    public ReceiptLine(String productDescription, int cost, BigDecimal tax, int discountAmount) {
        this.productDescription = productDescription;
        this.cost = cost;
        this.tax = tax;
        this.discountAmount = discountAmount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getCost() {
        return cost;
    }

    public String getFormattedCost() {
        return cost / 100d + " CHF";
    }

    public BigDecimal getTax() {
        return tax;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptLine that = (ReceiptLine) o;
        return cost == that.cost &&
                discountAmount == that.discountAmount &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(tax, that.tax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productDescription, cost, tax, discountAmount);
    }

    @Override
    public String toString() {
        return "ReceiptLine{" +
                "productDescription='" + productDescription + '\'' +
                ", cost=" + cost +
                ", tax=" + tax +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
