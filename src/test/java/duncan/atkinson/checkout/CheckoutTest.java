package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.dataobjects.Receipt;
import duncan.atkinson.dataobjects.ReceiptLine;
import duncan.atkinson.inventory.Inventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static duncan.atkinson.inventory.ProductId.*;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutTest {

    private Checkout checkout;

    private static Inventory inventory = new TestInventory();
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
    void should_calculateCost_givenSingleSimpleItem() {
        basket.addItem(PHONE_CASE);

        BigDecimal cost = checkout.calculateCost(basket);
        assertEquals(new BigDecimal(9), cost);
    }

    @Test
    void should_calculateCost_givenMultipleItems() {
        basket.addItem(PHONE_CASE);
        range(0, 10).forEach((i) -> basket.addItem(WIRED_EARPHONES));

        BigDecimal cost = checkout.calculateCost(basket);
        assertEquals(new BigDecimal(300_09), cost);
    }

    @Test
    void should_calculateTax_givenTaxableItem() {
        basket.addItem(PHONE_CASE);// CHF 00.09

        BigDecimal tax = checkout.calculateTax(basket);
        assertEquals(new BigDecimal(1), tax);// Not sure if this should be 0 or 1 here, 1
    }

    @Test
    void should_calculateTax_givenNonTaxableItem() {
        basket.addItem(PHONE_INSURANCE);

        BigDecimal tax = checkout.calculateTax(basket);
        assertEquals(new BigDecimal(0), tax);
    }

    @Test
    void should_calculateTax_givenMixtureOfTaxableAndNonTaxableItems() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(WIRELESS_EARPHONES);

        BigDecimal tax = checkout.calculateTax(basket);
        assertEquals(new BigDecimal(6_00), tax);
    }

    @Test
    void should_calculateCost_givenBOGOF() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        BigDecimal cost = checkout.calculateCost(basket);
        assertEquals(new BigDecimal(40_00), cost);
    }

    @Test
    void should_calculateTax_givenBOGOF() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        BigDecimal tax = checkout.calculateTax(basket);
        assertEquals(new BigDecimal(4_80), tax);
    }

    /**
     * Special discount in this case is where a discount of 20% applies when buying earphones
     */
    @Test
    void should_calculateCost_givenTaxonomyDiscountApplies() {
        basket.addItem(PHONE_INSURANCE);// 120
        basket.addItem(WIRED_EARPHONES);// 30
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        BigDecimal cost = checkout.calculateCost(basket);
        assertEquals(new BigDecimal(126_00), cost);
    }

    /**
     * Same as above but wireless
     */
    @Test
    void should_calculateCost_givenTaxonomyDiscountApplies_butWireless() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(WIRELESS_EARPHONES);
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        BigDecimal cost = checkout.calculateCost(basket);
        assertEquals(new BigDecimal(146_00), cost);
    }

    @Test
    void should_calculateCost_givenTaxonomyDiscountDoesNotApply() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(SIM_CARD);

        Receipt receipt = checkout.checkout(basket);
        assertEquals(new BigDecimal(140_00), receipt.getTotalCost());
    }

    @Test
    void should_calculateTax_givenTaxonomyDiscountApplies() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(WIRELESS_EARPHONES);
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        BigDecimal tax = checkout.calculateTax(basket);
        assertEquals(new BigDecimal(6_00), tax);
    }

    @Test
    void shouldFailTo_calculateCost_given_exceedingMaxItemLimit() {
        range(0, 11).forEach((i) -> basket.addItem(SIM_CARD));
        String msg = assertThrows(CheckoutException.class, () -> {
            checkout.checkout(basket);
        }).getMessage();
        assertEquals("SIM_CARD purchase limit is 10", msg);
    }

    @Test
    void should_checkout_givenSingleSimCard() {
        basket.addItem(SIM_CARD);
        Receipt receipt = checkout.checkout(basket);

        ReceiptLine expected = new ReceiptLine("Sim Card", new BigDecimal(2000), new BigDecimal(240), new BigDecimal(0));
        assertEquals(expected, receipt.getLines().get(0));
    }

    @Test
    void should_checkout_givenMultipleSimCards() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        Receipt receipt = checkout.checkout(basket);

        ReceiptLine first = receipt.getLines().get(0);
        ReceiptLine expected = new ReceiptLine("Sim Card * 3", new BigDecimal(4000), new BigDecimal(480), new BigDecimal(2000));
        assertEquals(expected, first);
    }

}