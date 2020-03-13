package duncan.atkinson.inventory;

import java.util.*;

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

    private Integer maximumPurchaseVolume;

    private Set<Taxonomy> taxonomy;

    private Product(Builder builder) {
        checkBuilderStateIsValid(builder);
        productId = builder.productId;
        name = builder.name;
        priceInCents = builder.priceInCents;
        taxExempt = builder.taxExempt;
        buyOneGetOneFree = builder.buyOneGetOneFree;
        taxonomyDiscount = builder.taxonomyDiscount;
        taxonomy = builder.taxonomy;
        maximumPurchaseVolume = builder.maximumPurchaseVolume;
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

    public Set<Taxonomy> getTaxonomy() {
        if(taxonomy == null){
            return Collections.emptySet();
        }else{
            return taxonomy;
        }
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

    public Integer getMaximumPurchaseVolume() {
        return maximumPurchaseVolume;
    }

    private void checkBuilderStateIsValid(Builder builder) {
        if (builder.productId == null) {
            throw new IllegalArgumentException("productId shouldn't be null");
        }
        if (builder.priceInCents == null) {
            throw new IllegalArgumentException("priceInCents shouldn't be null");
        }
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
                ", taxonomy=" + taxonomy +
                '}';
    }

    public static Builder aProduct() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId productId;
        private String name;
        private Integer priceInCents;
        private boolean taxExempt;
        private boolean buyOneGetOneFree;
        private TaxonomyDiscount taxonomyDiscount;
        private Integer maximumPurchaseVolume;
        private Set<Taxonomy> taxonomy;

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
            this.taxonomy = new HashSet<>(Arrays.asList(val));
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

        public Builder maximumPurchaseVolume(Integer val) {
            maximumPurchaseVolume = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
