import java.text.DecimalFormat;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.System.exit;

public class CashRegister extends ProductStorage {
    Double[][] coins;
    DecimalFormat df = new DecimalFormat("##.#");

    public CashRegister(Double[][] coins) {
        this.coins = coins;
    }

    public void showInitialCash() {
        System.out.println("Initial Cash Inventory: ");
        for (Double[] coin: coins) {
            System.out.print("Value: " + coin[0] + ",\t" + "Quantity: " + df.format(coin[1]) + "\n");
        }
        System.out.println("--------------------");
    }


    public void askAgain(double providedValue,int chosenProductIndex,ProductStorage storage) {
        Scanner s = new Scanner(System.in);
        double totalPaid = providedValue;
        double amountLeft = storage.products.get(chosenProductIndex).getPrice() - providedValue;
        boolean valueFlag = true;
        updateCashQuantity(providedValue);
        while (totalPaid < storage.products.get(chosenProductIndex).getPrice() ) {
            System.out.println("You paid " + df.format(totalPaid) + " in total. You still need to pay " + df.format(amountLeft));
            System.out.print("Provide bill or coin (accepted values: ");
            for (int i = 0; i < coins.length; i++) {
                if (i + 1 != coins.length) System.out.print(coins[i][0] + ", ");
                else System.out.println(coins[i][0] + ")");
            }
            System.out.println("Enter 'cancel' to cancel");
            String Value = s.nextLine();
            if (Value.equals("cancel")) {
                System.out.println("Transaction cancelled by user");
                break;
            }
            double newProvidedValue = Double.parseDouble(Value);
            int counter = 0;
            for (Double[] coin: coins) {
                if (newProvidedValue == coin[0]) {
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
            } catch (PayNotAcceptedException e) {
                System.out.println("The coin provided is not accepted.");
                e.printStackTrace();
                exit(1);
            }
                updateCashQuantity(newProvidedValue);
                totalPaid += newProvidedValue;
                totalPaid = Double.parseDouble(df.format(totalPaid));
                amountLeft = amountLeft - newProvidedValue;
                if ( totalPaid > storage.products.get(chosenProductIndex).getPrice()) {
                    System.out.println("You paid "+ df.format(totalPaid) +" in total. Your change will be " + df.format(abs(amountLeft)));
                    getChange(coins,abs(amountLeft));
                    System.out.println("Here is your product: "+ storage.products.get(chosenProductIndex).getName());
                    storage.updateProductQuantity(chosenProductIndex);
                    storage.showUpdatedProductList();
                    showUpdatedCashInventory();
                }
                if (totalPaid == storage.products.get(chosenProductIndex).getPrice()){
                    System.out.println("You paid "+ df.format(totalPaid) +" in total. Your change will be " + df.format(abs(amountLeft)));
                    System.out.println("Here is your product: "+ storage.products.get(chosenProductIndex).getName());
                    storage.updateProductQuantity(chosenProductIndex);
                    storage.showUpdatedProductList();
                    showUpdatedCashInventory();
                }
        }
    }

    public void getChange(Double[][] coins, Double changeNeeded) {
        Double[][] oldCoins = new Double[coins.length][];
        boolean coinsFlag = true;
        for (int i = 0; i < coins.length; i++) oldCoins[i] = coins[i].clone();

        for (int i = (coins.length - 1); i >= 0; i--) {
            if (coins[i][0] <= changeNeeded) {
                changeNeeded -= coins[i][0];
               coins[i][1]--;

            }
            if (coins[i][0] - changeNeeded < 0.0000001) i++;
            if (changeNeeded == 0) break;
        }
        for (int i = 0; i < coins.length; i++) {
            if (coins[i][1] <= 0) {
                coinsFlag = false;
                break;
            }
        }
        try {
            if (!coinsFlag) {
                throw new NotEnoughChangeException();
            }
        } catch (NotEnoughChangeException e) {
            System.out.println("The cash register does not have enough coins to complete this transaction.");
            e.printStackTrace();
            exit(1);
        }

        System.out.println("Here is your change: ");
            for (int i = 0; i < coins.length; i++) {
                if (oldCoins[i][1] > coins[i][1]) {
                    System.out.println("Value " + coins[i][0] + ", Quantity: " + (int)(oldCoins[i][1] - coins[i][1]));
                }
            }
        }

    public void updateCashQuantity(double providedValue) {
        for (int i = 0; i < coins.length; i++) {
            if (coins[i][0] == providedValue) {
                coins[i][1] += 1;
            }
        }
    }

    public void showUpdatedCashInventory() {
        System.out.println("Updated Cash Inventory:");
        for (Double[] coin: coins) {
            System.out.println("Value: " + coin[0] + ", quantity: " + coin[1]);
        }
        System.out.println("--------------------");
    }
}