package duncan.atkinson.inventory;

import java.util.HashMap;
import java.util.Map;

import static duncan.atkinson.inventory.Product.aProduct;
import static duncan.atkinson.inventory.ProductId.*;
import static duncan.atkinson.inventory.Taxonomy.*;

/**
 * Simple store of products we can sell indexed by {@link ProductId}
 * Expected to be replaced with a persistent store of some kind.
 *
 */
public class SimpleInventory implements Inventory {

    private Map<ProductId, Product> products;

    @Override
    public void init(){
        products = new HashMap<>();

        products.put(PHONE_CASE, aProduct()
                .productId(PHONE_CASE)
                .name("Phone Case")
                .priceInCents(1000)
                .taxonomy(MOBILE_PHONE_CASE)
                .build());

        products.put(SIM_CARD, aProduct()
                .productId(SIM_CARD)
                .name("Sim Card")
                .priceInCents(2000)
                .taxonomy(MOBILE_SIM_CARD)
                .build());

        products.put(PHONE_INSURANCE, aProduct()
                .productId(PHONE_INSURANCE)
                .name("Phone Insurance")
                .priceInCents(12000)
                .taxonomy(MOBILE_INSURANCE)
                .build());

        products.put(WIRED_EARPHONES, aProduct()
                .productId(WIRED_EARPHONES)
                .name("Wired Headphones")
                .priceInCents(30000)
                .taxonomy(EARPHONES)
                .build());

        products.put(WIRELESS_EARPHONES, aProduct()
                .productId(WIRELESS_EARPHONES)
                .name("Wireless Headphones")
                .priceInCents(50000)
                .taxonomy(EARPHONES, WIRELESS)
                .build());
    }

    @Override
    public Product get(ProductId productId) {
        return products.get(productId);
    }
}
