package org.gel.transactionsystem.expcetion;

public class NotEnoughBalanceException extends RuntimeException {

    public NotEnoughBalanceException(Long id) {
        super("User with id " + id + " does not have enough balance.");
    }
}
