package pos.machine;

import java.util.ArrayList;
import java.util.List;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ItemInfo> itemInfos = getAllItemsFromDB();
        int quantities[] = getQuantitiesOfItems(itemInfos, barcodes);
        String receipt = printItemDetails(itemInfos, quantities);
        return receipt;
    }

    public List<ItemInfo> getAllItemsFromDB(){
        return ItemDataLoader.loadAllItemInfos();
    }

    private int[] getQuantitiesOfItems(List<ItemInfo> itemInfos, List<String> barcodes) {
        int itemQuantities[] = new int[itemInfos.size()];
        for(int i=0; i<itemQuantities.length; i++){
            itemQuantities[i] = 0;
        }
        for(int j=0; j<barcodes.size(); j++){
            for(int k=0; k<itemInfos.size(); k++){
                if(barcodes.get(j).equals(itemInfos.get(k).getBarcode())){
                    itemQuantities[k] += 1;
                }
            }
        }

        return itemQuantities;
    }

    private String printItemDetails(List<ItemInfo> itemInfos, int[] quantities) {
        String receipt = "***<store earning no money>Receipt***\n";
        List<Integer> subtotals = new ArrayList<Integer>();

        for(int i=0; i<itemInfos.size(); i++){
            if(quantities[i] > 0){
                receipt += ("Name: "+itemInfos.get(i).getName()+", ");
                receipt += ("Quantity: "+quantities[i]+", ");
                receipt += ("Unit price: "+itemInfos.get(i).getPrice()+" (yuan), ");
                int subtotal = calculateSubtotal(itemInfos.get(i).getPrice(), quantities[i]);
                receipt += ("Subtotal: "+subtotal+" (yuan)\n");
                subtotals.add(subtotal);
            }
        }

        if(subtotals.size() > 0){
            receipt += "----------------------\n";
            receipt += ("Total: "+calculateTotal(subtotals)+" (yuan)\n");
            receipt += "**********************";
        }

        return receipt.equals("***<store earning no money>Receipt***\n") ? null : receipt;
    }

    private int calculateSubtotal(int price, int quantity) {
        return price * quantity;
    }

    private int calculateTotal(List<Integer> subtotals) {
        int total = 0;
        for(int subtotal : subtotals){
            total += subtotal;
        }
        return total;
    }
}
