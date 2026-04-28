package Auction.example.exception;

import Auction.example.enums.InvalidBidError;

public class InvalidBidException extends Exception {

    private final InvalidBidError errorCode;

    public InvalidBidException(InvalidBidError errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public InvalidBidException(InvalidBidError errorCode, String additionalDetail) {
        super(errorCode.getDescription() + " - " + additionalDetail);
        this.errorCode = errorCode;
    }

    public InvalidBidError getErrorCode() {
        return errorCode;
    }
}
