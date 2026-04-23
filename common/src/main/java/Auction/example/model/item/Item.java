package Auction.example.model.item;

import Auction.example.enums.ItemCondition;

public abstract class Item {
    protected String id;
    protected String name;
    protected String description;
    protected double startingPrice;
    protected ItemCondition condition;

    public Item(String id, String name, String description, double startingPrice, ItemCondition condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startingPrice = startingPrice;
        this.condition = condition;
    }

// getter
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getStartingPrice() {
        return startingPrice;
    }
    public ItemCondition getCondition() {
        return condition;
    }



    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public abstract String getDisplayInfo();

    public abstract boolean validateItem();

}
