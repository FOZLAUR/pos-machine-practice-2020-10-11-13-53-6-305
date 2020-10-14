package pos.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ItemInfo> itemInfos = getAllItemsFromDB();
        List<Integer> quantities = getQuantitiesOfItems(itemInfos, barcodes);
        String receipt = printItemDetails(itemInfos, quantities);
        return receipt;
    }

    public List<ItemInfo> getAllItemsFromDB(){
        return ItemDataLoader.loadAllItemInfos();
    }

    private List<Integer> getQuantitiesOfItems(List<ItemInfo> itemInfos, List<String> barcodes) {
        List<Integer> itemQuantities = new ArrayList<Integer>();
        for(ItemInfo itemInfo : itemInfos){
            itemQuantities.add(Collections.frequency(barcodes, itemInfo.getBarcode()));
        }

        return itemQuantities;
    }

    private String printItemDetails(List<ItemInfo> itemInfos, List<Integer> quantities) {
        String receipt = "***<store earning no money>Receipt***\n";
        List<Integer> subtotals = new ArrayList<Integer>();

        for(int i=0; i<itemInfos.size(); i++){
            int subtotal = calculateSubtotal(itemInfos.get(i).getPrice(), quantities.get(i));
            if(subtotal > 0){
                receipt += ("Name: "+itemInfos.get(i).getName()+", ");
                receipt += ("Quantity: "+quantities.get(i)+", ");
                receipt += ("Unit price: "+itemInfos.get(i).getPrice()+" (yuan), ");
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
        return subtotals.stream().reduce(0, Integer::sum);
    }
}
