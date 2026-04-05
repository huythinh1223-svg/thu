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

    public abstract String getDisplayInfo();
    public abstract boolean validateItem();

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
}
