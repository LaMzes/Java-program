import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static final String PROMOTION_30_OFF = "30% off";
    private static final String PROMOTION_BUY_2_GET_3RD_FREE = "Buy 2, get 3rd free";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Seller company = new Seller("Everyday Snacks");

        company.addClient(new Client(1, "ABC Distribution", 0.05, 0.00, 0.02));
        company.addClient(new Client(2, "DEF Foods", 0.04, 0.01, 0.02));
        company.addClient(new Client(3, "GHI Trade", 0.03, 0.01, 0.03));
        company.addClient(new Client(4, "JKL Kiosks", 0.02, 0.03, 0.05));
        company.addClient(new Client(5, "MNO Vending", 0.00, 0.05, 0.07));

        company.addProduct(new Product(1, "Danish Muffin", 0.52, 0.52 * 0.80, "none"));
        company.addProduct(new Product(2, "Granny's Cup Cake", 0.38, 0.38 * 1.20, PROMOTION_30_OFF));
        company.addProduct(new Product(3, "Frenchy's Croissant", 0.41, 0.90, "none"));
        company.addProduct(new Product(4, "Crispy chips", 0.60, 1.00, PROMOTION_BUY_2_GET_3RD_FREE));


        //TODO validations for valid input of string
        //- Element 1: the Client ID (currently 1 to 5) as first element
        //- Element 2-N: Concatenation of Product ID (1 to 4)=Quantity Ordered (1 to 1 million)

        // Accept order input
        System.out.println("Enter order (e.g., 1=100,2=200):");
        String orderInput = sc.nextLine();

        // Validate and process order
        processOrder(company, orderInput);

    }

    //TODO proper exception handling
    public static void processOrder(Seller company, String input) {
        try {
            String[] tokens = input.split(",");
            int clientId = Integer.parseInt(tokens[0]);

            Client client = company.getClientById(clientId);
            if (client == null) {
                System.out.println("Client with ID " + clientId + " not found.");
                return;
            }

            Map<Product, Integer> orderDetails = new HashMap<>();
            for (int i = 1; i < tokens.length; i++) {
                String[] parts = tokens[i].split("=");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                Product product = company.getProductById(productId);
                if (product != null) {
                    orderDetails.put(product, quantity);
                } else {
                    System.out.println("Product with ID " + productId + " not found.");
                }
            }

            double totalAmount = calculateTotalBeforeDiscounts(orderDetails);
            double basicDiscount = calculateBasicClientDiscount(totalAmount, client.getBasicClientDiscount());
            double totalAmountAfterBasicDiscount = totalAmount - basicDiscount;

            double additionalDiscount = 0.0;
            if (totalAmountAfterBasicDiscount >= 30000) {
                additionalDiscount = calculateAdditionalVolumeDiscount(totalAmountAfterBasicDiscount, client.getAbove30000Discount());
            } else if (totalAmountAfterBasicDiscount >= 10000) {
                additionalDiscount = calculateAdditionalVolumeDiscount(totalAmountAfterBasicDiscount, client.getAbove10000Discount());
            }

            double orderTotal = calculateOrderTotal(totalAmountAfterBasicDiscount,additionalDiscount);

            //TODO Round up properly in methods
            System.out.println("Client: " + client.getName());
            for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                System.out.println("Product: " + product.getName());
                System.out.println("Quantity: " + quantity);
                System.out.println("Standard Unit Price: " + String.format("%.2f", calculateStandardUnitPrice(product)));
                System.out.println("Promotional Unit Price: " + String.format("%.2f", calculatePromotionalUnitPrice(product, quantity)));
                System.out.println("Line Total: " + String.format("%.2f", calculateLineTotal(product, quantity)));
                System.out.println();
            }
            System.out.println("Total Before Client Discounts: EUR " + totalAmount);
            if (client.getBasicClientDiscount() > 0.00) {
                System.out.println("Basic Client Discount: EUR " + String.format("%.2f", basicDiscount));
            }
            if (client.getAbove10000Discount() > 0.00) {
                System.out.println("Additional Volume Discount: EUR " + String.format("%.2f", additionalDiscount));
            }
            System.out.println("Order Total Amount: EUR " + String.format("%.2f", orderTotal));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide a valid order format.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static double calculateOrderTotal(double totalAmountAfterBasicDiscount, double additionalDiscount) {
        return totalAmountAfterBasicDiscount - additionalDiscount;
    }

    private static double calculateAdditionalVolumeDiscount(double totalAmountAfterBasicDiscount, double discountRate) {
        return totalAmountAfterBasicDiscount * discountRate;
    }

    private static double calculateBasicClientDiscount(double totalAmount, double basicClientDiscount) {
        return totalAmount * basicClientDiscount;
    }

    private static double calculateLineTotal(Product product, int quantity) {
        return calculatePromotionalUnitPrice(product, quantity) * quantity;
    }

    private static double calculateStandardUnitPrice(Product product) {
        return product.getUnitCost() + product.getMarkup();
    }

    private static double calculatePromotionalUnitPrice(Product product, int quantity) {
        double standardUnitPrice = calculateStandardUnitPrice(product);
        double promotionalUnitPrice = standardUnitPrice; // Initialize with standard unit price
        switch (product.getPromotion()) {
            case PROMOTION_30_OFF:
                promotionalUnitPrice = standardUnitPrice * 0.70; // Apply 30% off promotion
                break;
            case PROMOTION_BUY_2_GET_3RD_FREE:
                // Adjust the quantity to account for the free items
                int setsOfThree = quantity / 3; // Calculate the number of sets of three
                int remainingItems = quantity % 3; // Calculate the number of remaining items
                int adjustedQuantity = (setsOfThree * 2) + remainingItems;
                // Calculate the total cost considering the free items
                double totalCostWithFreeItems = standardUnitPrice * adjustedQuantity;
                // Calculate the promotional unit price considering the free items
                promotionalUnitPrice = totalCostWithFreeItems / quantity;
                break;
        }
        return promotionalUnitPrice;
    }

    private static double calculateTotalBeforeDiscounts(Map<Product, Integer> orderDetails) {
        double totalAmount = 0.0;
        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalAmount += calculateLineTotal(product, quantity);
        }
        return totalAmount;
    }
}

//import java.util.Map;
//import java.util.HashMap;
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        // Create a company
//        Seller company = new Seller("Everyday Snacks");
//
//        // Add clients to the company
//        company.addClient(new Client(1, "ABC Distribution", 0.05, 0.00, 0.02));
//        company.addClient(new Client(2, "DEF Foods", 0.04, 0.01, 0.02));
//        company.addClient(new Client(3, "GHI Trade", 0.03, 0.01, 0.03));
//        company.addClient(new Client(4, "JKL Kiosks", 0.02, 0.03, 0.05));
//        company.addClient(new Client(5, "MNO Vending", 0.00, 0.05, 0.07));
//
//        // Add products to the company
//        company.addProduct(new Product(1, "Danish Muffin", 0.52, 0.52*0.80, "none"));
//        company.addProduct(new Product(2, "Granny's Cup Cake", 0.38, 0.38*1.20, "30% off"));
//        company.addProduct(new Product(3, "Frenchy's Croissant", 0.41, 0.90, "none"));
//        company.addProduct(new Product(4, "Crispy chips", 0.60, 1.00, "Buy 2, get 3rd free"));
//
//        String orderInput = sc.nextLine();
//        processOrder(company, orderInput);
//
//    }
//
//    public static void processOrder(Seller company, String input) {
//        // Implement order processing logic here
//        try {
//            // Parse client ID from input
//            String[] tokens = input.split(",");
//            int clientId = Integer.parseInt(tokens[0]);
//
//            // Lookup client by ID (assuming you have a method to do this)
//            Client client = company.getClientById(clientId);
//            if (client == null) {
//                System.out.println("Client with ID " + clientId + " not found.");
//                return;
//            }
//
//            // Parse order details from input
//            Map<Product, Integer> orderDetails = new HashMap<>();
//            for (int i = 1; i < tokens.length; i++) {
//                String[] parts = tokens[i].split("=");
//                int productId = Integer.parseInt(parts[0]);
//                int quantity = Integer.parseInt(parts[1]);
//                Product product = company.getProductById(productId);
//                if (product != null) {
//                    orderDetails.put(product, quantity);
//                } else {
//                    System.out.println("Product with ID " + productId + " not found.");
//                }
//            }
//
//            // Calculate total amount before discounts
//            double totalAmount = calculateTotalBeforeDiscounts(orderDetails);
//
//            // Calculate basic client discount
//            double basicDiscount = calculateBasicClientDiscount(totalAmount, client.getBasicClientDiscount());
//
//            double totalAmountAfterBasicDiscount = totalAmount - basicDiscount;
//            // Calculate additional volume discount
//
//            double additionalDiscountAboveThirtyThousand = calculateAdditionalVolumeDiscountAboveThirtyThousand(totalAmountAfterBasicDiscount, client.getAbove30000Discount());
//            double additionalDiscountAboveTenThousand = calculateAdditionalVolumeDiscountAboveTenThousand(totalAmountAfterBasicDiscount, client.getAbove10000Discount());
//
//            // Print order details and total
//            System.out.println("Client: " + client.getName());
//            for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
//                Product product = entry.getKey();
//                int quantity = entry.getValue();
//                System.out.println("Product: " + product.getName());
//                System.out.println("Quantity: " + quantity);
//                System.out.println("Standard Unit Price: " + String.format("%.2f", calculateStandardUnitPrice(product)));
//                System.out.println("Promotional Unit Price: " + String.format("%.2f", calculatePromotionalUnitPrice(product, quantity)));
//                System.out.println("Line Total: " + String.format("%.2f", calculateLineTotal(product, quantity)));
//                System.out.println();
//            }
//            System.out.println("Total Before Client Discounts: EUR " + totalAmount);
//            if (basicDiscount > 0.00) {
//                System.out.println("Basic Client Discount: EUR " + String.format("%.2f", basicDiscount));
//            }
//            if (totalAmount >= 30000) {
//                System.out.println("Additional Volume Discount: " +
//                        String.format("%.2f",calculateAdditionalVolumeDiscountAboveThirtyThousand(totalAmountAfterBasicDiscount, client.getAbove30000Discount())));
//                System.out.println("Order Total Amount: " +
//                        String.format("%.2f",calculateOrderTotal(totalAmountAfterBasicDiscount, additionalDiscountAboveThirtyThousand)));
//            } else if (totalAmount >= 10000) {
//                System.out.println("Additional Volume Discount: EUR " +
//                        String.format("%.2f", calculateAdditionalVolumeDiscountAboveTenThousand(totalAmountAfterBasicDiscount, client.getAbove10000Discount())));
//                System.out.println("Order Total Amount: " +
//                        String.format("%.2f", calculateOrderTotal(totalAmountAfterBasicDiscount, additionalDiscountAboveTenThousand)));
//            } else {
//                System.out.println("Order Total Amount: " +
//                        String.format("%.2f", totalAmountAfterBasicDiscount));
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid input format. Please provide a valid order format.");
//        } catch (Exception e) {
//            System.out.println("An unexpected error occurred: " + e.getMessage());
//        }
//    }
//
//    private static double calculateOrderTotal(double totalAmountAfterBasicDiscount, double additionalDiscount) {
//        return totalAmountAfterBasicDiscount - additionalDiscount;
//    }
//
//    private static double calculateAdditionalVolumeDiscountAboveTenThousand(double totalAmountAfterBasicDiscount, double above10000Discount) {
//        return totalAmountAfterBasicDiscount * above10000Discount;
//    }
//
//    private static double calculateAdditionalVolumeDiscountAboveThirtyThousand(double totalAmountAfterBasicDiscount, double above30000Discount) {
//        return totalAmountAfterBasicDiscount * above30000Discount;
//    }
//
//    private static double calculateBasicClientDiscount(double totalAmount, double basicClientDiscount) {
//        return totalAmount * basicClientDiscount;
//    }
//
//    private static double calculateLineTotal(Product product, int quantity) {
//        return calculatePromotionalUnitPrice(product, quantity) * quantity;
//    }
//
//    private static double calculateStandardUnitPrice(Product product) {
//        return product.getUnitCost() + product.getMarkup();
//    }
//
//    private static double calculatePromotionalUnitPrice(Product product, int quantity) {
//        double standardUnitPrice = calculateStandardUnitPrice(product);
//        double promotionalUnitPrice = standardUnitPrice; // Initialize with standard unit price
//        if (product.getPromotion().equals("30% off")) {
//            promotionalUnitPrice = standardUnitPrice * 0.70; // Apply 30% off promotion
//        } else if (product.getPromotion().equals("Buy 2, get 3rd free")) {
//            // Adjust the quantity to account for the free items
//            int setsOfThree = quantity / 3; // Calculate the number of sets of three
//            int remainingItems = quantity % 3; // Calculate the number of remaining items
//
//            // Calculate the adjusted quantity considering the free items
//            int adjustedQuantity = (setsOfThree * 2) + remainingItems;
//
//            // Calculate the total cost considering the free items
//            double totalCostWithFreeItems = standardUnitPrice * adjustedQuantity;
//
//            // Calculate the promotional unit price considering the free items
//            promotionalUnitPrice = totalCostWithFreeItems / quantity;
//        }
//        return promotionalUnitPrice;
//    }
//
//    private static double calculateTotalBeforeDiscounts(Map<Product, Integer> orderDetails) {
//        double totalAmount = 0.0;
//        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
//            Product product = entry.getKey();
//            int quantity = entry.getValue();
//            totalAmount += calculateLineTotal(product, quantity);
//        }
//        return totalAmount;
//    }
//
//}
