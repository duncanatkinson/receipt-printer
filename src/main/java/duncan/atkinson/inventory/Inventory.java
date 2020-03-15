package duncan.atkinson.inventory;

import java.util.List;

public interface Inventory {
    void init();

    Product get(ProductId productId);

    List<ProductId> getProductIdsWithTaxonomy(Taxonomy taxonomyTriggeringDiscount);
}
