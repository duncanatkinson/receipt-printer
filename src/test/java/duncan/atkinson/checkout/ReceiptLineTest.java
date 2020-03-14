package duncan.atkinson.checkout;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptLineTest {

    @Test
    void should_getFormattedCost() {
        ReceiptLine line = new ReceiptLine("test", 10_00, new BigDecimal(0), 0);

        String formattedCost = line.getFormattedCost();
        assertEquals("10.00 CHF", formattedCost);
    }

    @Test
    void should_getFormattedCostGivenOneCent() {
        ReceiptLine line = new ReceiptLine("test", 1, new BigDecimal(0), 0);

        String formattedCost = line.getFormattedCost();
        assertEquals("0.01 CHF", formattedCost);
    }

    @Test
    void should_getFormattedCostGivenMillion() {
        ReceiptLine line = new ReceiptLine("test", 1_000_000_00, new BigDecimal(0), 0);

        String formattedCost = line.getFormattedCost();
        assertEquals("1,000,000.00 CHF", formattedCost);
    }
}