package duncan.atkinson.inventory;

import java.util.Arrays;
import java.util.Objects;

/**
 * Defines a product which could be purchased.
 */
public class Product {

    private ProductId productId;

    private String name;

    private Integer priceInCents;

    /**
     * A Set of tags which give the product properties
     */
    private Taxonomy[] taxonomy;

    public Product(ProductId productId, String name, Integer priceInCents, Taxonomy... taxonomy) {

    }

    private Product(Builder builder) {
        if(builder.productId == null){
            throw new IllegalArgumentException("productId shouldn't be null");
        }
        if(builder.priceInCents == null){
            throw new IllegalArgumentException("priceInCents shouldn't be null");
        }
        this.productId = builder.productId;
        this.name = builder.name;
        this.priceInCents = builder.priceInCents;
        this.taxonomy = builder.taxonomy;
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPriceInCents() {
        return priceInCents;
    }

    public Taxonomy[] getTaxonomy() {
        return taxonomy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", priceInCents=" + priceInCents +
                ", taxonomy=" + Arrays.toString(taxonomy) +
                '}';
    }

    public static Builder aProduct(){
        return new Builder();
    }

    public static final class Builder {
        private ProductId productId;
        private String name;
        private Integer priceInCents;
        private Taxonomy[] taxonomy;

        public Builder() {
        }

        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder priceInCents(Integer val) {
            priceInCents = val;
            return this;
        }

        public Builder taxonomy(Taxonomy ... val) {
            taxonomy = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
