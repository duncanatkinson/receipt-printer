package duncan.atkinson.dataobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO implement all equals and hashcode tests
 *
 * @see <a href="https://jqno.nl/equalsverifier/">equalsverifier</a>
 */
class ReceiptLineTest {

    @Test
    void should_getCost() {
        ReceiptLine line1 = new ReceiptLine("test", new BigDecimal("10.00"), new BigDecimal(0), new BigDecimal(0));
        ReceiptLine line2 = new ReceiptLine("test", new BigDecimal("10.00"), new BigDecimal(0), new BigDecimal(0));

        assertEquals(line1, line2);
    }

}