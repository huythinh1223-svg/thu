package user.code;

import java.time.LocalDateTime;

public class Auction {
    private String auctionId;
    private String productName;
    private String sellerId;

    private double startingPrice;
    private double currentPrice;
    private String highestBidderId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private AuctionState state;

    public Auction(String auctionId, String productName, String sellerId,
                   double startingPrice, LocalDateTime startTime, LocalDateTime endTime) {
        this.auctionId = auctionId;
        this.productName = productName;
        this.sellerId = sellerId;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = AuctionState.OPEN;
    }

    public void updateState() {
        LocalDateTime now = LocalDateTime.now();

        if (state == AuctionState.OPEN && !now.isBefore(startTime)) {
            state = AuctionState.RUNNING;
        }

        if (state == AuctionState.RUNNING && !now.isBefore(endTime)) {
            state = AuctionState.FINISHED;
        }
    }

    public synchronized void placeBid(String bidderId, double bidAmount) throws Exception {
        updateState();

        if (state != AuctionState.RUNNING) {
            throw new Exception("Auction is not running");
        }

        if (bidAmount <= currentPrice) {
            throw new Exception("Bid must be higher than current price");
        }

        currentPrice = bidAmount;
        highestBidderId = bidderId;
    }

    public void markPaid() throws Exception {
        if (state != AuctionState.FINISHED) {
            throw new Exception("Auction must be finished before payment");
        }
        state = AuctionState.PAID;
    }

    public void cancelAuction() {
        state = AuctionState.CANCELED;
    }

    public AuctionState getState() {
        updateState();
        return state;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public String getHighestBidderId() {
        return highestBidderId;
    }
}