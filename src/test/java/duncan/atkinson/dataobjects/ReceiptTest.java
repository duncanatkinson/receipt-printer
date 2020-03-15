package duncan.atkinson.dataobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReceiptTest {

    @Test
    void should_getTotalCost() {
        Receipt receipt = aReceipt();
        assertEquals("16.80", receipt.getTotalCost().toString());
    }

    @Test
    void should_getTaxAmount() {
        Receipt receipt = aReceipt();
        assertEquals("1.80", receipt.getTaxAmount().toString());
    }

    @Test
    void getLines_should_returnImmutableList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            aReceipt().getLines().add(new ReceiptLine("a", new BigDecimal(10_00), new BigDecimal("120"), new BigDecimal(0)));
        });
    }

    private Receipt aReceipt() {
        return new Receipt(asList(
                new ReceiptLine("a", new BigDecimal(10_00), new BigDecimal("120"), new BigDecimal(0)),
                new ReceiptLine("b", new BigDecimal(5_00), new BigDecimal("60"), new BigDecimal(0))
        ), new BigDecimal("16.80"), new BigDecimal("1.80"));
    }
}