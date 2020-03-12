package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.SimpleInventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static duncan.atkinson.inventory.ProductId.*;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest {

    private Checkout checkout;

    private static Inventory inventory = new SimpleInventory();

    @BeforeAll
    static void beforeAll() {
        inventory.init();
    }

    @BeforeEach
    void setUp() {
        this.checkout = new Checkout(inventory);
    }

    @Test
    void should_calculateTotal_givenSingleSimpleItem() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(PHONE_CASE);

        int totalBeforeTaxInCents = checkout.calculateTotalBeforeTax(basket);
        assertEquals(10_00, totalBeforeTaxInCents);
    }

    @Test
    void should_calculateTotal_givenMultipleItems() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(PHONE_CASE);
        range(0,10).forEach((i) -> basket.addItem(WIRED_EARPHONES));

        int totalBeforeTax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(310_00, totalBeforeTax);
    }

    @Test
    void should_calculateTotal_givenTaxableItem() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(PHONE_CASE);// CHF10.00

        int tax = checkout.calculateTax(basket);
        assertEquals(1_20, tax);
    }

    @Test
    void should_calculateTax_givenNonTaxableItem() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(PHONE_INSURANCE);

        int tax = checkout.calculateTax(basket);
        assertEquals(0, tax);
    }

    @Test
    void should_calculateTotal_givenMixtureOfTaxableAndNonTaxableItems() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(PHONE_CASE);

        int tax = checkout.calculateTax(basket);
        assertEquals(1_20, tax);
    }

    @Test
    void should_calculateTotal_givenBOGOF() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        int totalBeforeTax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(40_00, totalBeforeTax);
    }
}