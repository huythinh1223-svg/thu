package user.code.common.src.main.java.Auction.example.model.auction;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;

    private String auctionId;
    private String bidderId;
    private double amount;
    private LocalDateTime time;

    public Bid(String auctionId, String bidderId, double amount) {
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.time = LocalDateTime.now(); // tự lấy thời điểm bid
    }

    // Getter
    public String getAuctionId() {
        return auctionId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("Bid[auction=%s, bidder=%s, amount=%.2f, time=%s]",
                auctionId, bidderId, amount, time);
    }
}