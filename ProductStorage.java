import java.util.ArrayList;

public class ProductStorage {
    public ArrayList<Product> products = new ArrayList<>();

    public void showInitialInventory(){
        System.out.println("--------------------");
        System.out.println("Initial Product Inventory: ");
        for (Product product : products) {

            System.out.print(product.getName().toUpperCase() + "\t" + "Quantity: " + product.getQuantity() + "\n");
        }
    }
    public void addProduct(String name, int quantity, double price){
        Product newProduct = new Product(name,quantity,price);
        products.add(newProduct);

    }
    public void showUpdatedProductList(){
        System.out.println("--------------------");
        System.out.println("Updated Product Inventory:");
        for (Product product : products) {
            System.out.println(product.getName() + " Quantity: " + product.getQuantity());
        }
    }
    public void updateProductQuantity(int chosenProductIndex){
        int previousQuantity = products.get(chosenProductIndex).getQuantity();
        products.get(chosenProductIndex).setQuantity(previousQuantity-1);
        }
    }
