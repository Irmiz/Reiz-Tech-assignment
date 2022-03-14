import java.text.DecimalFormat;
import java.util.Scanner;

import static java.lang.System.exit;

public class Supermarket {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Double[][] coins = new Double[][]{{0.1, 50.0}, {0.5, 50.0}, {1.0, 50.0}, {2.0, 50.0}};
        DecimalFormat df = new DecimalFormat("##.#");

        ProductStorage storage = new ProductStorage();
        CashRegister register = new CashRegister(coins);

        storage.addProduct("SODA", 10, 2.3);
        storage.addProduct("BREAD", 10, 1.1);
        storage.addProduct("WINE", 10, 2.7);

        storage.showInitialInventory();
        register.showInitialCash();

        boolean shopping = true;

        while(shopping){
            int count = 0;
            boolean nameFlag = false;
            int chosenProductIndex = 0;
            System.out.println("What would you like to buy? Type in the name of the desired product.");
            for (int i = 0; i < storage.products.size(); i++) {
                System.out.print(storage.products.get(i).getName().toUpperCase() +" (price: " + storage.products.get(i).getPrice() + ") ");
            }
            System.out.println();
            String productName = scanner.nextLine().toUpperCase();
            for (int i = 0; i < storage.products.size(); i++) {
                if(productName.equals(storage.products.get(i).getName())) {
                    chosenProductIndex = i;
                    count++;
                }
            }
            if(storage.products.get(chosenProductIndex).getQuantity() <= 0) nameFlag = true;
            if(count != 1) nameFlag = true;
            try {
                if (nameFlag){
                    throw new SoldOutException();
                }
            } catch (SoldOutException e){
                System.out.println("The product you are trying to buy is sold out or does not exist.");
                e.printStackTrace();
                exit(1);
            }

            System.out.println("You are trying to buy " + storage.products.get(chosenProductIndex).getName().toUpperCase() + "." + " You need to pay " + storage.products.get(chosenProductIndex).getPrice());
            System.out.print("Provide bill or coin (accepted values: ");
            for (int i = 0; i < coins.length; i++) {
                if (i + 1 != coins.length) System.out.print(coins[i][0] + ", ");
                else System.out.println(coins[i][0] + ")");
            }
            System.out.println("Enter 'cancel' to cancel");
            String Value = scanner.nextLine();
            if (Value.equals("cancel")) {
                System.out.println("Transaction cancelled by user");
                continue;
            }
            double providedValue = Double.parseDouble(Value);
            boolean valueFlag = true;
            int counter = 0;
            for (Double[] coin: coins) {
                if (providedValue == coin[0]) {
                    counter++;
                }
            }
            if (counter != 1) {
                valueFlag = false;
            }
            try {
                if (!valueFlag) {
                    throw new PayNotAcceptedException();
                }
            } catch (PayNotAcceptedException n) {
                System.out.println("The coin provided is not accepted.");
                n.printStackTrace();
                exit(1);
            }
            double changeNeeded = providedValue - storage.products.get(chosenProductIndex).getPrice();
            if(changeNeeded >= 0) {
                register.updateCashQuantity(providedValue);
                System.out.println("You paid "+ providedValue +" in total. Your change will be " + df.format(changeNeeded));
                register.getChange(coins,changeNeeded);
                System.out.println("Here is your product: "+ storage.products.get(chosenProductIndex).getName());
                storage.updateProductQuantity(chosenProductIndex);
                storage.showUpdatedProductList();
                register.showUpdatedCashInventory();
            }
            else register.askAgain(providedValue, chosenProductIndex,storage);

                boolean askContinueShopping = true;
                while(askContinueShopping){
                    System.out.println("Do you want to continue shopping? yes/no");
                    String stopShopping = scanner.nextLine().toUpperCase();

                    if (stopShopping.equals("NO")) {
                        shopping = false;
                        askContinueShopping = false;
                        System.out.println("Shopping stopped.");
                    }
                    else if(stopShopping.equals("YES")){
                        askContinueShopping = false;
                    }
                    else {
                        System.out.println("Invalid value.Try again.");
                    }
                }


            }


        }
    }



