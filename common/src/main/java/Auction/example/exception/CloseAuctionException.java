package Auction.example.exception;

public class CloseAuctionException extends RuntimeException {
    public CloseAuctionException(String reason, String id) {
        super(reason);
    }
}
