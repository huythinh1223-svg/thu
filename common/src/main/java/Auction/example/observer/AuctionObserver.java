package Auction.example.observer;

import Auction.example.model.auction.Bid;

public interface AuctionObserver {
    // Hàm này sẽ được gọi tự động khi có giá mới
    void updateNewBid(String auctionId, Bid newBid);
}