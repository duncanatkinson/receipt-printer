package duncan.atkinson;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.checkout.Checkout;
import duncan.atkinson.checkout.ReceiptPrinter;
import duncan.atkinson.dataobjects.Receipt;
import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.ProductId;
import duncan.atkinson.inventory.SimpleInventory;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Application {

    private Inventory inventory;
    private Checkout checkout;
    private ReceiptPrinter receiptPrinter;

    public Application() {
        inventory = new SimpleInventory();
        inventory.init();
        checkout = new Checkout(inventory);
        receiptPrinter = new ReceiptPrinter(40);
    }

    private void process(String[] args) {
        ShoppingBasket basket = new ShoppingBasket();
        for (String arg : args) {
            List<String> productIdStrings = productIdsAsStrings();
            if (productIdStrings.contains(arg)) {
                ProductId productId = ProductId.valueOf(arg);
                basket.addItem(productId);
            } else {
                System.out.println("'" + arg + "' is not a product");
            }
        }
        Receipt receipt = checkout.checkout(basket);
        String output = receiptPrinter.print(receipt);
        System.out.println(output);
    }

    private List<String> productIdsAsStrings() {
        return stream(ProductId.values())
                .map(ProductId::toString)
                .collect(toList());
    }

    public static void main(String[] args) {

        Application application = new Application();
        application.process(args);
    }
}
