package duncan.atkinson.checkout;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;

public class Receipt {

    private List<ReceiptLine> lines;

    private final BigDecimal totalCost;

    private BigDecimal taxAmount;

    public Receipt(List<ReceiptLine> lines, BigDecimal totalCost, BigDecimal taxAmount) {
        this.lines = lines;
        this.totalCost = totalCost;
        this.taxAmount = taxAmount;
    }

    public List<ReceiptLine> getLines() {
        return lines;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
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
