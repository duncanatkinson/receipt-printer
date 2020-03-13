package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.inventory.Inventory;
import duncan.atkinson.inventory.SimpleInventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duncan.atkinson.inventory.ProductId.*;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutTest {

    private Checkout checkout;

    private static Inventory inventory = new SimpleInventory();
    private ShoppingBasket basket;

    @BeforeAll
    static void beforeAll() {
        inventory.init();
    }

    @BeforeEach
    void setUp() {
        this.checkout = new Checkout(inventory);
        basket = new ShoppingBasket();
    }

    @Test
    void should_calculateTotalBeforeTax_givenSingleSimpleItem() {
        basket.addItem(PHONE_CASE);

        int totalBeforeTaxInCents = checkout.calculateTotalBeforeTax(basket);
        assertEquals(10_00, totalBeforeTaxInCents);
    }

    @Test
    void should_calculateTotalBeforeTax_givenMultipleItems() {
        basket.addItem(PHONE_CASE);
        range(0, 10).forEach((i) -> basket.addItem(WIRED_EARPHONES));

        int totalBeforeTax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(310_00, totalBeforeTax);
    }

    @Test
    void should_calculateTax_givenTaxableItem() {
        basket.addItem(PHONE_CASE);// CHF10.00

        int tax = checkout.calculateTax(basket);
        assertEquals(1_20, tax);
    }

    @Test
    void should_calculateTax_givenNonTaxableItem() {
        basket.addItem(PHONE_INSURANCE);

        int tax = checkout.calculateTax(basket);
        assertEquals(0, tax);
    }

    @Test
    void should_calculateTax_givenMixtureOfTaxableAndNonTaxableItems() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(PHONE_CASE);

        int tax = checkout.calculateTax(basket);
        assertEquals(1_20, tax);
    }

    @Test
    void should_calculateTotalBeforeTax_givenBOGOF() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        int tax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(40_00, tax);
    }

    @Test
    void should_calculateTax_givenBOGOF() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        int tax = checkout.calculateTax(basket);
        assertEquals(4_80, tax);
    }

    /**
     * Special discount in this case is where a discount of 20% applies when buying earphones
     */
    @Test
    void should_calculateTotalBeforeTax_givenTaxonomyDiscountApplies() {
        basket.addItem(PHONE_INSURANCE);// 120
        basket.addItem(WIRED_EARPHONES);// 30
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        int totalBeforeTax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(126_00, totalBeforeTax);
    }

    /**
     * Same as above but wireless
     */
    @Test
    void should_calculateTotalBeforeTax_givenTaxonomyDiscountApplies_butWireless() {
        basket.addItem(PHONE_INSURANCE);// 120
        basket.addItem(WIRELESS_EARPHONES);// 50
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        int totalBeforeTax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(146_00, totalBeforeTax);
    }

    @Test
    void should_calculateTotalBeforeTax_givenTaxonomyDiscountDoesNotApply() {
        basket.addItem(PHONE_INSURANCE);// 120
        basket.addItem(SIM_CARD);// 20

        int totalBeforeTax = checkout.calculateTotalBeforeTax(basket);
        assertEquals(140_00, totalBeforeTax);
    }


    @Test
    void should_calculateTax_givenTaxonomyDiscountApplies() {
        basket.addItem(PHONE_INSURANCE);// 120
        basket.addItem(WIRELESS_EARPHONES);// 50
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        int tax = checkout.calculateTax(basket);
        assertEquals(6_00, tax);
    }

    @Test
    void shouldFailTo_calculateTotalBeforeTax_given_breakingTheLaw() {
        range(0, 11).forEach((i) -> basket.addItem(SIM_CARD));
        String msg = assertThrows(CheckoutException.class, () -> {
            checkout.calculateTotalBeforeTax(basket);
        }).getMessage();
        assertEquals("SIM_CARD purchase limit is 10", msg);
    }

    @Test
    void should_checkout_givenSingleSimCard() {
        basket.addItem(SIM_CARD);
        Receipt receipt = checkout.checkout(basket);

        assertEquals(new ReceiptLine("Sim Card", 2000, 240, 0), receipt.getLines().get(0));
    }

    @Test
    void should_checkout_givenMultipleSimCards() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        Receipt receipt = checkout.checkout(basket);

        ReceiptLine actual = receipt.getLines().get(0);
        assertEquals(new ReceiptLine("Sim Card * 3", 4000, 480, 2000), actual);
    }
}