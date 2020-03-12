package duncan.atkinson.inventory;

public interface Inventory {
    void init();

    Product get(ProductId productId);
}
