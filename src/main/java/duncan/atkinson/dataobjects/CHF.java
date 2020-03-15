package duncan.atkinson.dataobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * CHF represents an amount of Currency, in particular the Swiss Franc
 *
 * It is immutable all functions such as {@link #multiply(String)} will return a new value
 * and not mutate the instance.
 *
 * TODO implement equals hashcode tests
 */
public class CHF {

    public static final String CURRENCY_STRING = "CHF";

    private final BigDecimal amount;

    private static NumberFormat pricePrintingFormat;

    static {
        pricePrintingFormat = NumberFormat.getInstance();
        pricePrintingFormat.setGroupingUsed(true);
        pricePrintingFormat.setMinimumFractionDigits(2);
        pricePrintingFormat.setMaximumFractionDigits(2);
        pricePrintingFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    public CHF(int amount) {
        this.amount = normalise(new BigDecimal(amount));
    }

    public CHF(BigDecimal amount) {
        this.amount = normalise(amount);
    }

    public CHF(String amount) {
        this.amount = normalise(new BigDecimal(amount));
    }

    private BigDecimal normalise(BigDecimal bigDecimal) {
        return bigDecimal.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal getAmount() {
        return amount;
    }

    public CHF multiply(String amount) {
        return new CHF(this.amount.multiply(new BigDecimal(amount)));
    }

    public String formatted(){
        return pricePrintingFormat.format(amount);
    }

    public static CHF add(CHF a, CHF b) {
        return new CHF(a.getAmount().add(b.getAmount()));
    }

    public CHF divide(int i) {
        return new CHF(this.amount.divide(new BigDecimal(i), RoundingMode.HALF_UP));
    }

    public CHF minus(CHF discountAmount) {
        return new CHF(this.amount.add(discountAmount.getAmount().negate()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CHF chf = (CHF) o;
        return Objects.equals(amount, chf.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return CURRENCY_STRING +"{" + pricePrintingFormat.format(amount) + '}';
    }

    public String currencyString() {
        return CURRENCY_STRING;
    }
}
