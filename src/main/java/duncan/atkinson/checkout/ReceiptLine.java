package duncan.atkinson.checkout;

import javax.swing.text.NumberFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

public class ReceiptLine {
    private final String productDescription;
    private final int cost;
    private final BigDecimal tax;
    private final int discountAmount;

    private final NumberFormat priceFormat;

    public ReceiptLine(String productDescription, int cost, BigDecimal tax, int discountAmount) {
        this.productDescription = productDescription;
        this.cost = cost;
        this.tax = tax;
        this.discountAmount = discountAmount;
        priceFormat =  NumberFormat.getInstance();
        priceFormat.setGroupingUsed(true);
        priceFormat.setMinimumFractionDigits(2);
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getCost() {
        return cost;
    }

    @Deprecated
    public String getFormattedCost() {
        BigDecimal costAsBigDecimal = new BigDecimal(cost).setScale(2,HALF_UP);
        double wholeUnits = costAsBigDecimal.divide(new BigDecimal(100), HALF_UP).doubleValue();
        return priceFormat.format(wholeUnits) + " CHF";
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
