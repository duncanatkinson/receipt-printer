package duncan.atkinson.checkout;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptTest {
    @Test
    void should_calculateTotal() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("a", 1000, 120, 0),
                new ReceiptLine("b", 500, 60, 0)
        ));
        assertEquals(1680, receipt.getTotal());
    }
}