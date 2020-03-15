package duncan.atkinson.checkout;

import duncan.atkinson.dataobjects.CHF;
import duncan.atkinson.dataobjects.Receipt;
import duncan.atkinson.dataobjects.ReceiptLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptPrinterTest {

    public static final CHF A_PRICE = new CHF("0");
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
                new ReceiptLine("Socks", new CHF("23.99"), new CHF("2.88"))
        ), new CHF("26.87"), new CHF("2.88"));
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
                new ReceiptLine("Socks", new CHF("23.99"), new CHF(0))
        ), A_PRICE, A_PRICE);
        String output = printer.print(receipt);
        assertEquals("Socks          23.99 CHF", output.split("\n")[1]);
    }

    @Test
    void print_shouldFormatOutput_givenVariableLengthDescriptionsAndPrices() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", new CHF("1.99"), new CHF(0)),
                new ReceiptLine("Sausages", new CHF("23.99"), new CHF(0))
        ), A_PRICE, A_PRICE);
        String output = printer.print(receipt);
        assertEquals("Socks           1.99 CHF", output.split("\n")[1]);
        assertEquals("Sausages       23.99 CHF", output.split("\n")[2]);
    }

    @Test
    void print_shouldFormatOutput_givenDiscount() {
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Pizza", new CHF(10), new CHF(0), new CHF(5), "SPECIAL")
        ), new CHF(222), new CHF(111));
        String output = printer.print(receipt);
        assertEquals("Pizza          10.00 CHF", output.split("\n")[1]);
        assertEquals("SPECIAL        -5.00 CHF", output.split("\n")[2]);
        assertEquals("Sales Tax     111.00 CHF", output.split("\n")[4]);
        assertEquals("Total         222.00 CHF", output.split("\n")[5]);
    }

    @Test
    void print_shouldFormatOutput_givenLongProductDescription() {
        printer = new ReceiptPrinter(40);
        Receipt receipt = new Receipt(singletonList(
                new ReceiptLine("Some comfortable and expensive socks", new CHF(1_000), new CHF(0))
        ), A_PRICE, A_PRICE);
        String output = printer.print(receipt);
        String firstLine = output.split("\n")[1];
        assertEquals(40, firstLine.length());
        assertEquals("Some comfortable and expen  1,000.00 CHF", firstLine);
    }

    @Test
    void print_shouldFormatWholeReceipt_givenLargePrices() {
        Receipt receipt = new Receipt(asList(
                new ReceiptLine("Socks", new CHF("1000.99"), new CHF("0")),
                new ReceiptLine("Sausages", new CHF("2300.99"), new CHF("0"))
        ), new CHF("1000000.00"), new CHF("1000.00"));
        printer = new ReceiptPrinter();
        String output = printer.print(receipt);
        assertEquals("========================\n" +
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
                new ReceiptLine("Socks", new CHF("0.01"), new CHF("0")),
                new ReceiptLine("Sausages", new CHF("0.01"), new CHF("0"))
        ), new CHF(".01"), new CHF(".01"));
        printer = new ReceiptPrinter();
        String output = printer.print(receipt);
        assertEquals("========================\n" +
                        "Socks           0.01 CHF\n" +
                        "Sausages        0.01 CHF\n" +
                        "========================\n" +
                        "Sales Tax       0.01 CHF\n" +
                        "Total           0.01 CHF\n"
                ,
                output);
    }
}