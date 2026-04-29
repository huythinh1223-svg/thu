package Auction.example.model.auction;

import Auction.example.enums.InvalidBidError;
import Auction.example.model.item.items.Item;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import Auction.example.exception.InvalidBidException;
import user.code.common.src.main.java.Auction.example.exception.CloseAuctionException;

public class Auction {

    public enum State {
        OPEN, // moi tao phien dau gia
        RUNNING, // phien dau gia dang dien ra
        FINISHED, // phien dau gia ket thuc
        PAID,
        CANCELED
    }

    private String currentAuctionId;
    private State state;

    private String sellerId;
    private String highestBidderId;
    private String winnerId;

    private Item autionItem;
    private double startPrice;
    private double currentPrice;

    private List<Bid> bidHistory;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration; // tinh theo minutes

    private double minIncrementalPrice;

    private transient ScheduledExecutorService executor;
    private transient ScheduledFuture<?> future; // nhiem vu tu dong close auction trong tuong lai

    public Auction(String currentAuctionId, String sellerId, double startPrice, long duration,
                   double minIncrementalPrice) {
        this.currentAuctionId = currentAuctionId;
        this.state = State.OPEN;
        this.sellerId = sellerId;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.minIncrementalPrice = minIncrementalPrice;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.duration = duration;

        this.bidHistory = new ArrayList<>();
    }

    // Start the auction
    public synchronized void start(){
        if (this.state != State.OPEN) {
            return;
        }

        this.startTime = LocalDateTime.now();
        this.endTime = startTime.plusMinutes(duration);

         this.state = State.RUNNING;
         autoCloseAuction();
    }

    public synchronized void cancel(String reason){
        if (this.state != State.CANCELED || this.state == State.PAID) {
            return;
        }

        state = State.CANCELED;

        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
    }

    // Auto close the auction if khong ai bid them hoac het thoi gian
    private void autoCloseAuction() {
        long remainingMillis = Duration.between(LocalDateTime.now(), endTime).toMillis();

        if (remainingMillis > 0) {
            future = executor.schedule(this::finish, remainingMillis, TimeUnit.MILLISECONDS);
        } else {
            finish();
        }
    }

    //Ket thuc phien dau gia
    public synchronized void finish(){
        if (this.state == State.FINISHED || this.state == State.PAID ||  this.state == State.CANCELED) {
            return;
        }

        state = State.FINISHED;

        if (highestBidderId != null){
            winnerId = highestBidderId;
        }
    }

    public synchronized void placeBid(String bidderId, double amount)
            throws InvalidBidException, CloseAuctionException {
        if (state != State.RUNNING) {
            throw new CloseAuctionException("Auction is not running", currentAuctionId);
        }

        if (LocalDateTime.now().isAfter(endTime)) {
            finish();
            throw new CloseAuctionException("Auction is already ended", currentAuctionId);
        }

        //if (amount <= currentPrice) {
            //throw new InvalidBidException("Bid must be higher than current price", currentPrice)
        //}

        if (amount < currentPrice + minIncrementalPrice) {
            throw new InvalidBidException(
                    InvalidBidError.INVALID_INCREMENT,
                    "Giá đặt phải cao hơn giá hiện tại cộng với bước giá tối thiểu (" + minIncrementalPrice + ")"
            );
        }

        currentPrice = amount;
        highestBidderId = bidderId;

        Bid bid = new Bid(currentAuctionId, bidderId, amount);
        bidHistory.add(bid);
    }

    public synchronized boolean processPayment(String winnerId, double amount) {
        if (!winnerId.equals(this.winnerId)) {
            return false;
        }

        if (Math.abs(amount - currentPrice) < 0.01) {
            state = State.PAID;
            return true;
        }
        return false;
    }


}