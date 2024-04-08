import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testProcessOrderValidInput() {
        Seller company = new Seller("TestSeller");
        company.addClient(new Client(1, "TestClient", 0.05, 0.00, 0.02));
        company.addProduct(new Product(1, "TestProduct", 1.0, 0.5, "none"));

        String validInput = "1=10";
        Main.processOrder(company, validInput);
        // Add assertions here to verify the expected output
    }

    @Test
    public void testProcessOrderInvalidClientId() {
        Seller company = new Seller("TestSeller");
        company.addClient(new Client(1, "TestClient", 0.05, 0.00, 0.02));
        company.addProduct(new Product(1, "TestProduct", 1.0, 0.5, "none"));

        String invalidClientIdInput = "0=10";
        Main.processOrder(company, invalidClientIdInput);
        // Add assertions here to verify the expected error message

        String nonExistingClientIdInput = "2=10";
        Main.processOrder(company, nonExistingClientIdInput);
        // Add assertions here to verify the expected error message
    }

    @Test
    public void testProcessOrderInvalidProductIds() {
        Seller company = new Seller("TestSeller");
        company.addClient(new Client(1, "TestClient", 0.05, 0.00, 0.02));
        company.addProduct(new Product(1, "TestProduct", 1.0, 0.5, "none"));

        String invalidProductIdInput = "1=10,5=20";
        Main.processOrder(company, invalidProductIdInput);
        // Add assertions here to verify the expected error message

        String nonExistingProductIdInput = "2=10";
        Main.processOrder(company, nonExistingProductIdInput);
        // Add assertions here to verify the expected error message
    }

    @Test
    public void testProcessOrderInvalidQuantities() {
        Seller company = new Seller("TestSeller");
        company.addClient(new Client(1, "TestClient", 0.05, 0.00, 0.02));
        company.addProduct(new Product(1, "TestProduct", 1.0, 0.5, "none"));

        String invalidQuantityInput = "1=0";
        Main.processOrder(company, invalidQuantityInput);
        // Add assertions here to verify the expected error message

        String negativeQuantityInput = "1=-5";
        Main.processOrder(company, negativeQuantityInput);
        // Add assertions here to verify the expected error message
    }
}
