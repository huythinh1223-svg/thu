package Auction.example.exception;

public class AuctionClosedException extends RuntimeException {
    public AuctionClosedException(String reason, String id) {
        super(reason);
    }
}
