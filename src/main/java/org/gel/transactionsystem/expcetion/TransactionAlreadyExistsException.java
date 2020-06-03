package org.gel.transactionsystem.expcetion;

public class TransactionAlreadyExistsException extends RuntimeException {

    public TransactionAlreadyExistsException(Long id) {
        super(String.format("Transaction with id %d already exists", id));
    }
}
