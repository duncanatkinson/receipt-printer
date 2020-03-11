package duncan.atkinson;

import duncan.atkinson.basket.ShoppingBasket;
import org.junit.jupiter.api.Test;

import static duncan.atkinson.Product.PHONE_CASE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingBasketTest {

    @Test
    void shouldAddItemToBasket() {
        ShoppingBasket basket = new ShoppingBasket();
        basket.addItem(PHONE_CASE);

        assertEquals(basket.getProductsInBasket().size(), 1);
    }
}