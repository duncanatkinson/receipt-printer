package duncan.atkinson.checkout;

import duncan.atkinson.inventory.SimpleInventory;
import duncan.atkinson.inventory.TaxonomyDiscount;

import java.util.HashMap;

import static duncan.atkinson.inventory.Product.aProduct;
import static duncan.atkinson.inventory.ProductId.*;
import static duncan.atkinson.inventory.Taxonomy.*;

class TestInventory extends SimpleInventory {
    @Override
    public void init() {
        this.products = new HashMap<>();

        products.put(PHONE_CASE, aProduct()
                .productId(PHONE_CASE)
                .name("Phone Case")
                .priceInCents(9)
                .taxonomy(MOBILE_PHONE_CASE)
                .build());

        products.put(SIM_CARD, aProduct()
                .productId(SIM_CARD)
                .name("Sim Card")
                .priceInCents(20_00)
                .taxonomy(MOBILE_SIM_CARD)
                .buyOneGetOneFree(true)
                .maximumPurchaseVolume(10)
                .build());

        products.put(PHONE_INSURANCE, aProduct()
                .productId(PHONE_INSURANCE)
                .name("Phone Insurance")
                .priceInCents(120_00)
                .taxonomy(INSURANCE)
                .taxExempt(true)
                .taxonomyDiscount(new TaxonomyDiscount(20, EARPHONES))
                .build());

        products.put(WIRED_EARPHONES, aProduct()
                .productId(WIRED_EARPHONES)
                .name("Wired Headphones")
                .priceInCents(30_00)
                .taxonomy(EARPHONES)
                .build());

        products.put(WIRELESS_EARPHONES, aProduct()
                .productId(WIRELESS_EARPHONES)
                .name("Wireless Headphones")
                .priceInCents(50_00)
                .taxonomy(EARPHONES, WIRELESS)
                .build());
    }
}
