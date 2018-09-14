package mygrocerylist.listandshop;

public class Items {

    private String itemName;
    private Integer noOfArray;
    private Integer itemQt;

    public Items (String itemName, Integer noOfArray, Integer itemQt){
        this.itemName = itemName;
        this.noOfArray = noOfArray;
        this.itemQt = itemQt;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getNoOfArray() {
        return noOfArray;
    }

    public Integer getItemQt() {
        return itemQt;
    }


}
