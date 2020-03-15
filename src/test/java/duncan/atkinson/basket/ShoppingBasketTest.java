package duncan.atkinson.basket;

import duncan.atkinson.dataobjects.OrderLine;
import duncan.atkinson.inventory.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static duncan.atkinson.inventory.ProductId.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingBasketTest {

    private ShoppingBasket basket;

    @BeforeEach
    void setUp() {
        basket = new ShoppingBasket();
    }

    @Test
    void should_countItemsInBasket_given_EmptyBasket() {
        assertEquals(0, basket.countItemsInBasket());
    }

    @Test
    void should_countItemsInBasket_given_SingleItemsOfDifferentProductsInBasket() {
        basket.addItem(PHONE_CASE);
        basket.addItem(WIRELESS_EARPHONES);
        assertEquals(2, basket.countItemsInBasket());
    }

    @Test
    void should_countItemsInBasket_given_MultipleItemsOfSameProductInBasket() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        basket.addItem(PHONE_CASE);
        basket.addItem(PHONE_CASE);
        assertEquals(4, basket.countItemsInBasket());
    }

    @Test
    void should_getContents_given_MultipleItemsOfSameProductInBasket() {
        basket.addItem(SIM_CARD);
        basket.addItem(SIM_CARD);
        basket.addItem(PHONE_CASE);
        basket.addItem(PHONE_CASE);

        List<ProductId> contents = basket.getContents();
        assertEquals(4, contents.size());
    }

    @Test
    void should_getOrderLines_given_multipleProducts() {
        basket.addItem(WIRED_EARPHONES);
        basket.addItem(WIRELESS_EARPHONES);
        basket.addItem(WIRED_EARPHONES);
        basket.addItem(WIRELESS_EARPHONES);
        basket.addItem(PHONE_CASE);

        List<OrderLine> orderLines = basket.getOrderLines();
        assertEquals(3, orderLines.size());
        assertTrue(orderLines.containsAll(asList(
                new OrderLine(WIRELESS_EARPHONES, 2),
                new OrderLine(WIRED_EARPHONES, 2),
                new OrderLine(PHONE_CASE, 1)
        )));
    }
}