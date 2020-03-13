package duncan.atkinson.basket;

import duncan.atkinson.inventory.ProductId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineTest {

    @Test
    void should_construct() {
        new OrderLine(ProductId.PHONE_INSURANCE,1);
    }

    @Test
    void shouldNot_construct_given_productIdIsNull() {
        assertThrows(IllegalArgumentException.class,() -> {
          new OrderLine(null,1);
        });
    }

    @Test
    void shouldNot_construct_given_CountIsLessThanOne() {
        assertThrows(IllegalArgumentException.class,() -> {
            new OrderLine(ProductId.PHONE_INSURANCE,0);
        });
    }
}