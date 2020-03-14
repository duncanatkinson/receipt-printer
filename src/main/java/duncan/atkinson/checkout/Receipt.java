package duncan.atkinson.checkout;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;

public class Receipt {
    public static final String CURRENCY = " CHF";

    private List<ReceiptLine> lines;

    public Receipt(List<ReceiptLine> lines) {

        this.lines = lines;
    }

    public List<ReceiptLine> getLines() {
        return lines;
    }

    public String getTaxAsFormattedPrice() {
        return getTax().divide(new BigDecimal(100), HALF_UP) + CURRENCY;
    }

    public String getTotalAsFormattedPrice() {
        int cost = lines.stream()
                .map(ReceiptLine::getCost)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal total = new BigDecimal(cost).add(getTax());
        return total.divide(new BigDecimal(100), HALF_UP).toString() + CURRENCY;
    }

    /**
     * @return amount of tax for the whole receipt in cents
     */
    private BigDecimal getTax() {
        return lines.stream()
                .map(ReceiptLine::getTax)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0))
                .setScale(2, HALF_UP);
    }
}
