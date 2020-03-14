package duncan.atkinson.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptPrinterTest {

    public static final BigDecimal A_PRICE = new BigDecimal("0");
    private ReceiptPrinter printer;

    @BeforeEach
    void setUp() {
        printer = new ReceiptPrinter();
    }

    @Test
    void should_construct() {
        new ReceiptPrinter();
    }

    @Test
    void should_construct_givenSetWidth() {
        new ReceiptPrinter(50);
    }

    @Test
    void shouldNot_construct_givenHugeWidth() {
        assertThrows(IllegalArgumentException.class, () -> new ReceiptPrinter(1000));
    }

    @Test
    void shouldNot_construct_givenTinyWidth() {
        assertThrows(IllegalArgumentException.class, () -> new ReceiptPrinter(20));
    }

    @Test
    void print_should_printCorrectValues() {
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Socks", 2399, new BigDecimal(288), 0)
        ), new BigDecimal("26.87"), new BigDecimal("2.88"));
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
        ), A_PRICE, A_PRICE);
        String output = printer.print(receipt);
        assertEquals("Socks          23.99 CHF", output.substring(0, 24));
    }

    @Test
    void print_shouldFormatOutput_givenVariableLengthDescriptionsAndPrices() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", 199, new BigDecimal("23.88"), 0),
                new ReceiptLine("Sausages", 2399, new BigDecimal("287.88"), 0)
        ), A_PRICE, A_PRICE);
        String output = printer.print(receipt);
        assertEquals("Socks           1.99 CHF", output.substring(0, 24));
        assertEquals("Sausages       23.99 CHF", output.substring(25, 49));
    }

    @Test
    void print_shouldFormatOutput_givenLongProductDescription() {
        printer = new ReceiptPrinter(40);
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Some comfortable and expensive socks", 1_000_00, new BigDecimal("12000"), 0)
        ), A_PRICE, A_PRICE);
        String output = printer.print(receipt);
        String firstLine = output.substring(0, output.indexOf(System.lineSeparator()));
        assertEquals(40, firstLine.length());
        assertEquals("Some comfortable and expen  1,000.00 CHF", firstLine);
    }

    @Test
    void print_shouldFormatWholeReceipt_givenLargePrices() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", 1_000_99, new BigDecimal("0"), 0),
                new ReceiptLine("Sausages", 2_300_99, new BigDecimal("0"), 0)
        ), new BigDecimal("1000000.00"), new BigDecimal("1000.00"));
        printer = new ReceiptPrinter();
        String output = printer.print(receipt);
        assertEquals("" +
                        "Socks       1,000.99 CHF\n" +
                        "Sausages    2,300.99 CHF\n" +
                        "========================\n" +
                        "Sales Tax   1,000.00 CHF\n" +
                        "Total   1,000,000.00 CHF\n"
                ,
                output);
    }

    @Test
    void print_shouldFormatWholeReceipt_givenSmallPrices() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", 1, new BigDecimal("0"), 0),
                new ReceiptLine("Sausages", 1, new BigDecimal("0"), 0)
        ), new BigDecimal(".01"), new BigDecimal(".01"));
        printer = new ReceiptPrinter();
        String output = printer.print(receipt);
        assertEquals("" +
                        "Socks           0.01 CHF\n" +
                        "Sausages        0.01 CHF\n" +
                        "========================\n" +
                        "Sales Tax       0.01 CHF\n" +
                        "Total           0.01 CHF\n"
                ,
                output);
    }
}