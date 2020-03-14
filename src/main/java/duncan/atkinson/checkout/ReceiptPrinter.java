package duncan.atkinson.checkout;

public class ReceiptPrinter {

    public static final String CURRENCY = " CHF";

    public String print(Receipt receipt) {
        StringBuilder output = new StringBuilder();
        receipt.getLines().forEach(line ->{
            output.append(line.getProductDescription());
            output.append(line.getCost() / 100D).append(" CHF");

        });
        output.append("Sales Tax");
        output.append(receipt.getTax() / 100D).append(CURRENCY);
        output.append("Total");
        output.append(receipt.getTotal() / 100D).append(CURRENCY);
        return output.toString();
    }
}
