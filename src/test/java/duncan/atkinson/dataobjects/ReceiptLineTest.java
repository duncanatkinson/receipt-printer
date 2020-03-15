package duncan.atkinson.dataobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO implement all equals and hashcode tests
 *
 * @see <a href="https://jqno.nl/equalsverifier/">equalsverifier</a>
 */
class ReceiptLineTest {

    @Test
    void should_getCost() {
        ReceiptLine line1 = new ReceiptLine("test", new CHF("10.00"), new CHF(0), new CHF(0));
        ReceiptLine line2 = new ReceiptLine("test", new CHF("10.00"), new CHF(0), new CHF(0));

        assertEquals(line1, line2);
    }

}