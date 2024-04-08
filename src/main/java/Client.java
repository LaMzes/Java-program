public class Client {

    private int clientId;
    private String name;
    private double basicClientDiscount;
    private double above10000Discount;
    private double above30000Discount;

    public Client(int clientId, String name, double basicClientDiscount, double above10000Discount, double above30000Discount) {
        this.clientId = clientId;
        this.name = name;
        this.basicClientDiscount = basicClientDiscount;
        this.above10000Discount = above10000Discount;
        this.above30000Discount = above30000Discount;
    }

    public int getClientId() {
        return clientId;
    }

    public String getName() {
        return this.name;
    }

    public double getBasicClientDiscount() {
        return this.basicClientDiscount;
    }

    public double getAbove10000Discount() {
        return this.above10000Discount;
    }

    public double getAbove30000Discount() {
        return this.above30000Discount;
    }

    public double calculateBasicClientDiscount(double totalAmount) {
        return totalAmount * basicClientDiscount;
    }

    public double calculateAdditionalVolumeDiscount(double totalAmount) {
        if (totalAmount > 30000) {
            return totalAmount * above30000Discount;
        } else if (totalAmount > 10000) {
            return totalAmount * above10000Discount;
        }
        return 0;
    }

    public int getId() {
        return this.clientId;
    }
}