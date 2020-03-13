package duncan.atkinson.inventory;

import java.util.Set;

public interface Inventory {
    void init();

    Product get(ProductId productId);

    Set<ProductId> getProductIdsWithTaxonomy(Taxonomy taxonomyTriggeringDiscount);
}
