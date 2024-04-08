public class Product {

    private int productId;
    private String name;
    private double unitCost;
    private double markup;
    private String promotion;

    public Product(int productId, String name, double unitCost, double markup, String promotion) {
        this.productId = productId;
        this.name = name;
        this.unitCost = unitCost;
        this.markup = markup;
        this.promotion = promotion;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getMarkup() {
        return markup;
    }

    public String getPromotion() {
        return promotion;
    }

    public int getId() {
        return productId;
    }
}
