package duncan.atkinson.inventory;

import org.junit.jupiter.api.Test;

import static duncan.atkinson.inventory.Product.aProduct;
import static duncan.atkinson.inventory.ProductId.PHONE_CASE;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void should_construct() {
        aValidProduct()
                .build();
    }

    @Test
    void shouldNot_construct_given_nullProductId() {
        String message = assertThrows(IllegalArgumentException.class, () ->
                aValidProduct()
                        .productId(null)
                        .build())
                .getMessage();
        assertEquals("productId shouldn't be null", message);
    }

    @Test
    void shouldNot_construct_given_nullPrice() {
        String message = assertThrows(IllegalArgumentException.class, () ->
                aValidProduct()
                        .priceInCents(null)
                        .build())
                .getMessage();
        assertEquals("priceInCents shouldn't be null", message);
    }

    @Test
    void should_haveTaxonomyDiscount_givenTaxonomyDiscount() {
        Product product = aValidProduct()
                .taxonomyDiscount(new TaxonomyDiscount(10, Taxonomy.WIRELESS))
                .build();
        assertTrue(product.hasTaxonomyDiscount());
    }

    @Test
    void shouldNot_haveTaxonomyDiscount_givenTaxonomyDiscount() {
        Product product = aValidProduct()
                .taxonomyDiscount(null)
                .build();
        assertFalse(product.hasTaxonomyDiscount());
    }

    private Product.Builder aValidProduct() {
        return aProduct()
                .productId(PHONE_CASE)
                .name("name")
                .priceInCents(100);
    }
}