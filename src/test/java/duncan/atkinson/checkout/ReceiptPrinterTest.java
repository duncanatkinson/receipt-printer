package duncan.atkinson.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiptPrinterTest {

    private ReceiptPrinter printer;

    @BeforeEach
    void setUp() {
        printer = new ReceiptPrinter();
    }

    @Test
    void print_should_printCorrectValues() {
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Socks", 2399, 288, 0)
        ));
        String output = printer.print(receipt);

        assertTrue(output.contains("Socks"));
        assertTrue(output.contains("23.99 CHF"));
        assertTrue(output.contains("Sales Tax"));
        assertTrue(output.contains("2.88 CHF"));
        assertTrue(output.contains("Total"));
        assertTrue(output.contains("26.87 CHF"));
    }

    @Test
    void shouldPrint() {
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Socks", 2399, 239, 0)
        ));
        String output = printer.print(receipt);

        String expectedOutput =
                        "Socks          23.99 CHF\n" +
                        "========================" +
                        "Sales Tax       2.88 CHF\n" +
                        "Total          26.87 CHF\n";
        assertEquals(expectedOutput, output);
    }
}