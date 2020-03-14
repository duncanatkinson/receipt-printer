package duncan.atkinson.checkout;

import java.util.List;
import java.util.stream.Stream;

public class Receipt {
    private List<ReceiptLine> lines;

    public Receipt(List<ReceiptLine> lines) {

        this.lines = lines;
    }

    public List<ReceiptLine> getLines() {
        return lines;
    }

    public int getTotal() {
        return lines.stream()
                .map(line -> line.getCost() + line.getTax())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int getTax() {
        return lines.stream()
                .map(ReceiptLine::getTax)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
