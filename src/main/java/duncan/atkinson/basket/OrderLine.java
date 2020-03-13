package duncan.atkinson.basket;

import duncan.atkinson.inventory.ProductId;

import java.util.Objects;

public class OrderLine {

    private ProductId productId;

    private int count;

    public OrderLine(ProductId productId, int count) {
        if(productId == null ) throw new IllegalArgumentException("productId is null");
        if(count < 1 ) throw new IllegalArgumentException("count must be 1 or greater");
        this.productId = productId;
        this.count = count;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return count == orderLine.count &&
                productId == orderLine.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, count);
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "productId=" + productId +
                ", count=" + count +
                '}';
    }
}
