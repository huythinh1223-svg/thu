package user.code;

import java.time.LocalDateTime;

public class Bid {
    private Bidder bidder;
    private double amount;
    private LocalDateTime timestamp;

    public Bid(Bidder bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Bidder getBidder() { return bidder; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Bid[User: %s, Amount: %.2f, Time: %s]",
                bidder.getUsername(), amount, timestamp);
    }
}
