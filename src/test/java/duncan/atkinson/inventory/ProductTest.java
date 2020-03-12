package duncan.atkinson.inventory;

import org.junit.jupiter.api.Test;

import static duncan.atkinson.inventory.ProductId.PHONE_CASE;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void should_construct() {
        new Product(PHONE_CASE, "name", 100);
    }

    @Test
    void shouldNot_construct_given_nullProductId() {
        String message = assertThrows(IllegalArgumentException.class, () ->
                new Product(null, "name", 100))
                .getMessage();
        assertEquals("productId shouldn't be null", message);
    }

    @Test
    void shouldNot_construct_given_nullPrice() {
        String message = assertThrows(IllegalArgumentException.class, () ->
                new Product(PHONE_CASE, "name", null))
                .getMessage();
        assertEquals("priceInCents shouldn't be null", message);
    }
}