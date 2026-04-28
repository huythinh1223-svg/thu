package Auction.example.model.auction;

import user.code.Bid;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class BidHistory {
    private List<Bid> bids;

    public BidHistory() {
        this.bids = new ArrayList<>();
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public List<Bid> getAllBids() {
        return Collections.unmodifiableList(bids);
    }

    public int getTotalBids() {
        return bids.size();
    }    
}
