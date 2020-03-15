package duncan.atkinson;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.checkout.Checkout;
import duncan.atkinson.checkout.CheckoutException;
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

    /**
     * @param args the product items to purchase
     * @return the output string to aid testing primarily
     */
    protected String process(String[] args) {
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
        String output;
        try {
            Receipt receipt = checkout.checkout(basket);
            output = receiptPrinter.print(receipt);
        } catch (CheckoutException checkoutException) {
            output = String.format("Unable to checkout because: %s Please adjust your basket.", checkoutException.getMessage());
        }
        System.out.println(output);
        return output;
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
