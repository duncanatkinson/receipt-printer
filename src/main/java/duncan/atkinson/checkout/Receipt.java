package duncan.atkinson.checkout;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Object to hold data
 */
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
        return Collections.unmodifiableList(lines);
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}
