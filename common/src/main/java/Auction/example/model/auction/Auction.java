package Auction.example.model.auction;

import Auction.example.enums.AuctionState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Auction {
    private final String auctionId;
    private final String productName;
    private final String sellerId;

    private final double startingPrice;
    private double currentPrice;
    private Bidder highestBidder;

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private AuctionState state;
    private final List<Bid> bidHistory = new ArrayList<>();

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

    // Cập nhật trạng thái dựa theo thời gian hiện tại
    private synchronized void updateState() {
        LocalDateTime now = LocalDateTime.now();
        if (state == AuctionState.OPEN && !now.isBefore(startTime)) {
            state = AuctionState.RUNNING;
        }
        if (state == AuctionState.RUNNING && !now.isBefore(endTime)) {
            state = AuctionState.FINISHED;
        }
    }

    // Đặt giá — nhận Bidder object thay vì String để nhất quán với class Bid
    public synchronized void placeBid(Bidder bidder, double bidAmount) throws Exception {
        updateState();

        if (state != AuctionState.RUNNING) {
            throw new Exception("Auction is not running. Current state: " + state);
        }
        if (bidAmount <= currentPrice) {
            throw new Exception(String.format(
                    "Bid %.2f must be higher than current price %.2f", bidAmount, currentPrice));
        }

        Bid bid = new Bid(bidder, bidAmount);
        bidHistory.add(bid);
        currentPrice = bidAmount;
        highestBidder = bidder;
    }

    public void markPaid() throws Exception {
        if (state != AuctionState.FINISHED) {
            throw new Exception("Auction must be FINISHED before payment. Current state: " + state);
        }
        state = AuctionState.PAID;
    }

    public void cancelAuction() {
        if (state == AuctionState.PAID) {
            throw new IllegalStateException("Cannot cancel an already paid auction");
        }
        state = AuctionState.CANCELED;
    }

    // Getters
    public String getAuctionId()        { return auctionId; }
    public String getProductName()      { return productName; }
    public String getSellerId()         { return sellerId; }
    public double getStartingPrice()    { return startingPrice; }
    public double getCurrentPrice()     { return currentPrice; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime()   { return endTime; }
    public Bidder getHighestBidder()    { return highestBidder; }

    public AuctionState getState() {
        updateState();
        return state;
    }

    // Trả về bản copy để tránh bên ngoài modify trực tiếp list
    public List<Bid> getBidHistory() {
        return Collections.unmodifiableList(bidHistory);
    }

    @Override
    public String toString() {
        return String.format("Auction[id=%s, product=%s, price=%.2f, state=%s]",
                auctionId, productName, currentPrice, getState());
    }


// getter
    public String getAuctionId() {return auctionId;}
    public String getProductName() {return productName;}
    public String getSellerId() {return sellerId;}
    public double getStartingPrice() {return startingPrice;}
    public LocalDateTime getStartTime() {return startTime;}
    public LocalDateTime getEndTime() {return endTime;}
}