package org.gel.transactionsystem.expcetion;

public class TransactionAlreadyExistsException extends RuntimeException {

    public TransactionAlreadyExistsException(Long id) {
        super("Transaction with id " + id + " already exists");
    }
}
