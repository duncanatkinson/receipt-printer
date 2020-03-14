package duncan.atkinson.checkout;

import java.math.BigDecimal;
import java.text.NumberFormat;

import static java.lang.System.lineSeparator;

public class ReceiptPrinter {

    private static final String SPACES = "                                                                              ";
    private static final String SEPARATOR_CHARACTERS = "================================================================";

    private final int targetPrintoutWidth;

    private NumberFormat priceFormat;

    /**
     * Construct a receipt printer with a default width of 24 characters
     */
    public ReceiptPrinter() {
        this(24);
    }

    public ReceiptPrinter(int targetPrintoutWidth) {
        if (targetPrintoutWidth > SPACES.length()) {
            throw new IllegalArgumentException("Too wide, printer cannot print wider than" + SPACES.length());
        }
        if (targetPrintoutWidth < 21) {
            throw new IllegalArgumentException("Too narrow, printer cannot print narrower than" + 21);
        }
        this.targetPrintoutWidth = targetPrintoutWidth;
        priceFormat =  NumberFormat.getInstance();
        priceFormat.setGroupingUsed(true);
        priceFormat.setMinimumFractionDigits(2);
    }

    public String print(Receipt receipt) {
        StringBuilder output = new StringBuilder();
        receipt.getLines().forEach(line -> {
            outPutItemLine(output, line);
        });
        output.append(separator()).append(lineSeparator());
        output.append(insertPadding("Sales Tax", formatPrice(receipt.getTaxAmount())));
        output.append(lineSeparator());
        output.append(insertPadding("Total", formatPrice(receipt.getTotalCost())));
        output.append(lineSeparator());
        return output.toString();
    }

    //
//    @Deprecated
//    public String getTaxAsFormattedPrice() {
//        return getTax().divide(new BigDecimal(100), HALF_UP) + CURRENCY;
//    }
//
//    @Deprecated
//    public String getTotalAsFormattedPrice() {
//        int cost = lines.stream()
//                .map(ReceiptLine::getCost)
//                .mapToInt(Integer::intValue)
//                .sum();
//        BigDecimal total = new BigDecimal(cost).add(getTax());
//        return total.divide(new BigDecimal(100), HALF_UP).toString() + CURRENCY;
//    }

    private String formatPrice(BigDecimal taxAmount) {
        return priceFormat.format(taxAmount) + " CHF";
    }

    private String separator() {
        return SEPARATOR_CHARACTERS.substring(0, targetPrintoutWidth);
    }

    private void outPutItemLine(StringBuilder output, ReceiptLine line) {
        String amount = line.getFormattedCost();
        String formattedLineItem = insertPadding(line.getProductDescription(), amount);
        output.append(formattedLineItem);
        output.append(lineSeparator());
    }

    private String insertPadding(String part1, String part2) {
        int nonPaddingCharacterWidth = part1.length() + part2.length();
        int paddingWidth = targetPrintoutWidth - nonPaddingCharacterWidth;

        String padding = "";
        if (paddingWidth > 0) {
            padding = SPACES.substring(0, paddingWidth);
        } else {
            paddingWidth = -paddingWidth;// make it positive
            part1 = part1.substring(0, (part1.length() - paddingWidth) - 2) + "  ";
        }

        return part1 + padding + part2;
    }
}
