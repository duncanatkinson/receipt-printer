package duncan.atkinson.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
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
                new ReceiptLine("Socks", 2399, new BigDecimal(288), 0)
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
    void print_shouldFormatOutput() {
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Socks", 2399, new BigDecimal(239), 0)
        ));
        String output = printer.print(receipt);
        assertEquals("Socks          23.99 CHF", output.substring(0, 24));
    }

    @Test
    void print_shouldFormatOutput_givenVariableLengthDescriptionsAndPrices() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", 199, new BigDecimal("23.88"), 0),
                new ReceiptLine("Sausages", 2399, new BigDecimal("287.88"), 0)
        ));
        String output = printer.print(receipt);
        assertEquals("Socks           1.99 CHF", output.substring(0, 24));
        assertEquals("Sausages       23.99 CHF", output.substring(25, 49));
    }

    @Test
    void print_shouldFormatWholeReceipt() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", 199, new BigDecimal("23.88"), 0),
                new ReceiptLine("Sausages", 2399, new BigDecimal("287.88"), 0)
        ));
        String output = printer.print(receipt);
        assertEquals(""+
                "Socks           1.99 CHF\n"+
                "Sausages       23.99 CHF\n"+
                "========================\n"+
                "Sales Tax       3.12 CHF\n"+
                "Total          29.10 CHF\n"
                ,
                output);
    }
}