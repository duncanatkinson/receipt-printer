package duncan.atkinson;

import duncan.atkinson.basket.ShoppingBasket;
import duncan.atkinson.inventory.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static duncan.atkinson.inventory.ProductId.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Map<ProductId, Long> countsOfProducts = basket.getOrderLines();
        assertEquals(2,countsOfProducts.get(WIRED_EARPHONES).intValue());
        assertEquals(2,countsOfProducts.get(WIRELESS_EARPHONES).intValue());
        assertEquals(1,countsOfProducts.get(PHONE_CASE).intValue());
    }
}