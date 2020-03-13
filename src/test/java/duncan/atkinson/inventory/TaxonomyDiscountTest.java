package duncan.atkinson.inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaxonomyDiscountTest {

    @Test
    void shouldConstruct() {
        new TaxonomyDiscount(1, Taxonomy.MOBILE_PHONE_CASE);
    }

    @Test
    void shouldFailToConstruct_given_nullTaxonomy() {
        String message = assertThrows(IllegalArgumentException.class, () ->
                new TaxonomyDiscount(1, null)
        ).getMessage();
        assertEquals("taxonomy cannot be null", message);
    }
}