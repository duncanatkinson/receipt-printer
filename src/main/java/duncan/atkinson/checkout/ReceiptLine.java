package duncan.atkinson.checkout;

import java.util.Objects;

public class ReceiptLine {
    private String productDescription;
    private int cost;
    private int tax;
    private int discountAmount;

    public ReceiptLine(String productDescription, int cost, int tax, int discountAmount) {
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

    public int getTax() {
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
                tax == that.tax &&
                discountAmount == that.discountAmount &&
                Objects.equals(productDescription, that.productDescription);
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
