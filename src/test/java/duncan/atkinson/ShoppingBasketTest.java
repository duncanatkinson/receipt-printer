package duncan.atkinson;

import duncan.atkinson.basket.ShoppingBasket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duncan.atkinson.Product.PHONE_CASE;
import static duncan.atkinson.Product.WIRELESS_EARPHONES;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingBasketTest {

    private ShoppingBasket basket;

    @BeforeEach
    void setUp() {
        basket = new ShoppingBasket();
    }

    @Test
    void addItemsToBasket_should() {
        basket.addItem(PHONE_CASE);
        basket.addItem(WIRELESS_EARPHONES);
        basket.addItem(WIRELESS_EARPHONES);

        assertEquals(basket.getProductsInBasket().size(), 2);
    }

    @Test
    void hasProduct() {
        basket.addItem(WIRELESS_EARPHONES);
        assertTrue(basket.hasProduct(WIRELESS_EARPHONES));
        assertFalse(basket.hasProduct(PHONE_CASE));
    }
}