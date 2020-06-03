package org.gel.transactionsystem.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DefaultTransactionRequest {
    private long id;
    private long amount;
    private long transactionId;
}
