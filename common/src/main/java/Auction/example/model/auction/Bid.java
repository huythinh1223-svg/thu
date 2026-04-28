package Auction.example.model.auction;

import java.io.Serializable;
import java.time.LocalDateTime;

// update thêm Serializable cho mạng, Comparable cho sắp xếp
public class Bid implements Serializable, Comparable<Bid> {
    
    private static final long serialVersionUID = 1L;

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

    // Logic hỗ trợ sắp xếp giá thầu
    @Override
    public int compareTo(Bid other) {
        int priceComparison = Double.compare(this.amount, other.amount);
        if (priceComparison == 0) {
            // Nếu giá bằng nhau, ai đặt trước (thời gian nhỏ hơn) xếp trên
            return this.timestamp.compareTo(other.timestamp);
        }
        return priceComparison;
    }

    @Override
    public String toString() {
        return String.format("Bid[User: %s, Amount: %.2f, Time: %s]",
                bidder.getUsername(), amount, timestamp);
    }
}
