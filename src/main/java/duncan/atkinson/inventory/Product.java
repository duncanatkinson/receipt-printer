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

    private boolean taxExempt;

    private boolean buyOneGetOneFree;

    private TaxonomyDiscount taxonomyDiscount;

    /**
     * A Set of tags which give the product properties
     */
    private Taxonomy[] taxonomy;


    private Product(Builder builder) {
        checkBuilderStateIsValid(builder);
        productId = builder.productId;
        name = builder.name;
        priceInCents = builder.priceInCents;
        taxExempt = builder.taxExempt;
        buyOneGetOneFree = builder.buyOneGetOneFree;
        taxonomyDiscount = builder.taxonomyDiscount;
        taxonomy = builder.taxonomy;
    }

    public static Builder newBuilder(Product copy) {
        Builder builder = new Builder();
        builder.productId = copy.getProductId();
        builder.name = copy.getName();
        builder.priceInCents = copy.getPriceInCents();
        builder.taxExempt = copy.getTaxExempt();
        builder.buyOneGetOneFree = copy.getBuyOneGetOneFree();
        builder.taxonomyDiscount = copy.getTaxonomyDiscount();
        builder.taxonomy = copy.getTaxonomy();
        return builder;
    }

    private void checkBuilderStateIsValid(Builder builder) {
        if(builder.productId == null){
            throw new IllegalArgumentException("productId shouldn't be null");
        }
        if(builder.priceInCents == null){
            throw new IllegalArgumentException("priceInCents shouldn't be null");
        }
    }

    public static Builder newBuilder() {
        return new Builder();
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

    public boolean getTaxExempt() {
        return taxExempt;
    }

    public boolean getBuyOneGetOneFree() {
        return buyOneGetOneFree;
    }

    public TaxonomyDiscount getTaxonomyDiscount() {
        return taxonomyDiscount;
    }

    public boolean hasTaxonomyDiscount() {
        return taxonomyDiscount != null;
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
        private boolean taxExempt;
        private boolean buyOneGetOneFree;
        private TaxonomyDiscount taxonomyDiscount;
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

        public Builder taxExempt(boolean val) {
            taxExempt = val;
            return this;
        }

        public Builder taxonomy(Taxonomy... val) {
            taxonomy = val;
            return this;
        }

        public Builder buyOneGetOneFree(boolean b) {
            buyOneGetOneFree = b;
            return this;
        }

        public Builder taxonomyDiscount(TaxonomyDiscount val) {
            taxonomyDiscount = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
