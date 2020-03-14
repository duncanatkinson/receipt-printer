package duncan.atkinson.checkout;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptTest {

    @Test
    void should_getTotalAsFormattedPrice() {
        Receipt receipt = aReceipt();
        assertEquals("16.80 CHF", receipt.getTotalAsFormattedPrice());
    }

    @Test
    void should_getTaxAsFormattedPrice() {
        Receipt receipt = aReceipt();
        assertEquals("1.80 CHF", receipt.getTaxAsFormattedPrice());
    }

    private Receipt aReceipt() {
        return new Receipt(asList(
                new ReceiptLine("a", 10_00, new BigDecimal("120"), 0),
                new ReceiptLine("b", 5_00, new BigDecimal("60"), 0)
        ));
    }
}