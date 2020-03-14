package duncan.atkinson.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.System.lineSeparator;

public class ReceiptPrinter {

    public static final int TARGET_PRINTOUT_WIDTH = 24;
    public static final String SPACES = "                                                                              ";
    public static final String SEPARATOR_CHARACTERS = "================================================================";

    public String print(Receipt receipt) {
        StringBuilder output = new StringBuilder();
        receipt.getLines().forEach(line -> {
            outPutItemLine(output, line);
        });
        output.append(separator()).append(lineSeparator());
        output.append(insertPadding("Sales Tax", receipt.getTaxAsFormattedPrice()));
        output.append(lineSeparator());
        output.append(insertPadding("Total",receipt.getTotalAsFormattedPrice()));
        output.append(lineSeparator());
        return output.toString();
    }

    private String separator() {
        return SEPARATOR_CHARACTERS.substring(0, TARGET_PRINTOUT_WIDTH);
    }

    private void outPutItemLine(StringBuilder output, ReceiptLine line) {
        String amount = line.getFormattedCost();
        String formattedLineItem = insertPadding(line.getProductDescription(), amount);
        output.append(formattedLineItem);
        output.append(lineSeparator());
    }

    private String insertPadding(String part1, String part2) {
        int nonPaddingCharacterWidth = part1.length() + part2.length();
        int paddingWidth = TARGET_PRINTOUT_WIDTH - nonPaddingCharacterWidth;
        String padding = SPACES.substring(0, paddingWidth);
        return part1 + padding + part2;
    }
}
