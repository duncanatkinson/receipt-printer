package duncan.atkinson.dataobjects;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReceiptTest {

    @Test
    void should_getTotalCost() {
        Receipt receipt = aReceipt();
        assertEquals("CHF{16.80}", receipt.getTotalCost().toString());
    }

    @Test
    void should_getTaxAmount() {
        Receipt receipt = aReceipt();
        assertEquals("CHF{1.80}", receipt.getTaxAmount().toString());
    }

    @Test
    void getLines_should_returnImmutableList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            aReceipt().getLines().add(new ReceiptLine("a", new CHF(10_00), new CHF("120")));
        });
    }

    private Receipt aReceipt() {
        return new Receipt(asList(
                new ReceiptLine("a", new CHF(10_00), new CHF("120")),
                new ReceiptLine("b", new CHF(5_00), new CHF("60"))
        ), new CHF("16.80"), new CHF("1.80"));
    }
}