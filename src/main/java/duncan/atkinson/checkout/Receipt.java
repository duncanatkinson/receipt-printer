package duncan.atkinson.checkout;

import java.util.List;

public class Receipt {
    private List<ReceiptLine> lines;

    public Receipt(List<ReceiptLine> lines) {

        this.lines = lines;
    }

    public String getReceiptString() {
        return "";
    }

    public List<ReceiptLine> getLines() {
        return lines;
    }
}
