package  user.code.common.src.main.java.Auction.example.model.auction;

import Auction.example.enums.InvalidBidError;
import Auction.example.model.item.items.Item;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import user.code.common.src.main.java.Auction.example.exception.InvalidBidException;
import user.code.common.src.main.java.Auction.example.exception.AuctionClosedException;
import user.code.common.src.main.java.Auction.example.exception.AuctionException;
import user.code.common.src.main.java.Auction.example.exception.InvalidBidException;
import user.code.common.src.main.java.Auction.example.observer.AuctionObserver;
import Auction.example.model.auction.Bid;
import user.code.common.src.main.java.Auction.example.observer.AuctionObserver;

public class Auction implements Serializable {
    private static final long serialVersionUID = 1L;

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

    private transient List<AuctionObserver> observers;
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

    // Bổ sung các hàm Getter hỗ trợ Unit Test và UI
    public State getState() {
        return state;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public List<Bid> getBidHistory() {
        return bidHistory;
    }

    public String getHighestBidderId() {
        return highestBidderId;
    }

    // Start the auction
    public synchronized void start(){
        if (this.state != State.OPEN) {
            return;
        }

        this.startTime = LocalDateTime.now();
        this.endTime = startTime.plusMinutes(duration);

         this.state = State.RUNNING;
         StartAuctionNotifier();
         autoCloseAuction();
    }

    public synchronized void cancel(String reason){
        if (this.state != State.CANCELED || this.state == State.FINISHED || this.state == State.PAID) {
            return;
        }

        state = State.CANCELED;

        if (future != null && !future.isDone()) {
            future.cancel(false);
        }

        CancelAuctionNotifier(reason);
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

        FinishAuctionNotifier(winnerId, currentPrice);
    }

    public synchronized void placeBid(String bidderId, double amount)
            throws InvalidBidException, AuctionClosedException {
        if (state != State.RUNNING) {
            throw new AuctionClosedException("Auction is not running", currentAuctionId);
        }

        if (LocalDateTime.now().isAfter(endTime)) {
            finish();
            throw new AuctionClosedException("Auction is already ended", currentAuctionId);
        }

        if (amount < currentPrice + minIncrementalPrice) {
            throw new InvalidBidException("Your bid is too low", amount, currentPrice + minIncrementalPrice);
        }

        currentPrice = amount;
        highestBidderId = bidderId;

        Bid bid = new Bid(currentAuctionId, bidderId, amount);
        bidHistory.add(bid);

        BidPlacedNotifier(bidderId, amount);
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

    // Tao observer
    public void addObserver(AuctionObserver observer) {
        if (observers == null) {
            observers = new CopyOnWriteArrayList<>();
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void  removeObserver(AuctionObserver observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    public void BidPlacedNotifier(String bidderId, double amount) {
        if (observers != null){
            for (AuctionObserver observer : observers) {
                observer.onBidPlaced(currentAuctionId, bidderId, amount);
            }
        }

    }

    public void StartAuctionNotifier() {
        if (observers != null){
            for (AuctionObserver observer : observers) {
                observer.onAuctionStarted(currentAuctionId);
            }
        }
    }

    public void CancelAuctionNotifier(String reason) {
        if (observers != null){
            for (AuctionObserver observer : observers) {
             observer.onAuctionCanceled(currentAuctionId, reason);
            }
        }
    }

    public void FinishAuctionNotifier(String WinnerId, double finalPrice) {
        if (observers != null){
            for (AuctionObserver observer : observers) {
                observer.onAuctionFinished(currentAuctionId, WinnerId, finalPrice);
            }
        }
    }


}