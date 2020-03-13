package duncan.atkinson.inventory;

/**
 * Discount applies if item in basket matches taxonomy label
 */
public class TaxonomyDiscount {

    int percentageDiscount;

    /**
     * If another item in the basket matches this taxonomy then apply the discount.
     */
    Taxonomy taxonomy;

    public TaxonomyDiscount(int percentageDiscount, Taxonomy taxonomy) {
        if (taxonomy == null) {
            throw new IllegalArgumentException("taxonomy cannot be null");
        }
        this.percentageDiscount = percentageDiscount;
        this.taxonomy = taxonomy;
    }

    public int getPercentageDiscount() {
        return percentageDiscount;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }
}
