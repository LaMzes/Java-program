import java.util.ArrayList;
import java.util.List;

public class Seller {
    private String name;
    private List<Client> clients;
    private List<Product> products;

    public Seller(String name) {
        this.name = name;
        this.clients = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    // Methods to add clients and products
    public void addClient(Client client) {
        clients.add(client);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    // Method to get a client by ID
    public Client getClientById(int clientId) {
        for (Client client : clients) {
            if (client.getId() == clientId) {
                return client;
            }
        }
        return null; // Client not found
    }

    public Product getProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null; // Product not found
    }
}

