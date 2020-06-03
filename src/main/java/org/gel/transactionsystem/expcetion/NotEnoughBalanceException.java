package org.gel.transactionsystem.expcetion;

public class NotEnoughBalanceException extends RuntimeException {

    public NotEnoughBalanceException(Long id) {
        super(String.format("User with id %d does not have enough balance", id));
    }
}
