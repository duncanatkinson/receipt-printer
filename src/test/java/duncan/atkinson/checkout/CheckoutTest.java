package duncan.atkinson.checkout;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.dataobjects.CHF;
import duncan.atkinson.dataobjects.Receipt;
import duncan.atkinson.dataobjects.ReceiptLine;
import duncan.atkinson.inventory.Inventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        CHF cost = checkout.calculateCost(basket);
        assertEquals(new CHF("0.09"), cost);
    }

    @Test
    void should_calculateCost_givenMultipleItems() {
        basket.addItem(PHONE_CASE);
        range(0, 10).forEach((i) -> basket.addItem(WIRED_EARPHONES));

        CHF cost = checkout.calculateCost(basket);
        assertEquals(new CHF("300.09"), cost);
    }

    @Test
    void should_calculateTax_givenCheapTaxableItem() {
        basket.addItem(PHONE_CASE);// CHF 00.09

        CHF tax = checkout.checkout(basket).getTaxAmount();
        assertEquals(new CHF("0.01080"), tax);
    }

    @Test
    void should_calculateTax_givenNonTaxableItem() {
        basket.addItem(PHONE_INSURANCE);

        CHF tax = checkout.checkout(basket).getTaxAmount();
        assertEquals(new CHF(0), tax);
    }

    @Test
    void should_calculateTax_givenMixtureOfTaxableAndNonTaxableItems() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(WIRELESS_EARPHONES);

        CHF tax = checkout.checkout(basket).getTaxAmount();
        assertEquals(new CHF(6), tax);
    }

    @Test
    void should_calculateCost_givenBOGOF() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        CHF cost = checkout.calculateCost(basket);
        assertEquals(new CHF(40), cost);
    }

    @Test
    void should_calculateTax_givenBOGOF() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);//Free
        basket.addItem(SIM_CARD);

        CHF tax = checkout.checkout(basket).getTaxAmount();
        assertEquals(new CHF("4.80"), tax);
    }

    /**
     * Special discount in this case is where a discount of 20% applies when buying earphones
     */
    @Test
    void should_calculateCost_givenTaxonomyDiscountApplies() {
        basket.addItem(PHONE_INSURANCE);// 120
        basket.addItem(WIRED_EARPHONES);// 30
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        CHF cost = checkout.calculateCost(basket);
        assertEquals(new CHF(126), cost);
    }

    /**
     * Same as above but wireless
     */
    @Test
    void should_calculateCost_givenTaxonomyDiscountApplies_butWireless() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(WIRELESS_EARPHONES);
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        CHF cost = checkout.calculateCost(basket);
        assertEquals(new CHF(146), cost);
    }

    @Test
    void should_calculateCost_givenTaxonomyDiscountDoesNotApply() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(SIM_CARD);

        Receipt receipt = checkout.checkout(basket);
        assertEquals(new CHF("142.40"), receipt.getTotalCost());
    }

    @Test
    void should_calculateTax_givenTaxonomyDiscountApplies() {
        basket.addItem(PHONE_INSURANCE);
        basket.addItem(WIRELESS_EARPHONES);
        // we expect a discount of 20% (24) on your phone insurance bringing it down to 96

        CHF tax = checkout.checkout(basket).getTaxAmount();
        assertEquals(new CHF(6), tax);
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

        ReceiptLine expected = new ReceiptLine("Sim Card", new CHF("20.00"), new CHF("2.40"));
        assertEquals(expected.getCost(), receipt.getLines().get(0).getCost());
        assertEquals(expected.getTax(), receipt.getLines().get(0).getTax());
        assertEquals(expected, receipt.getLines().get(0));
    }

    @Test
    void should_checkout_givenMultipleSimCards() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        Receipt receipt = checkout.checkout(basket);

        ReceiptLine actual = receipt.getLines().get(0);
        ReceiptLine expected = new ReceiptLine("Sim Card * 3", new CHF(60), new CHF("4.80"), new CHF(20), "BOGOF");
        assertEquals(expected, actual);
        assertEquals(new CHF("4.80"), receipt.getTaxAmount());
        assertEquals(new CHF("44.80"), receipt.getTotalCost());
    }

}