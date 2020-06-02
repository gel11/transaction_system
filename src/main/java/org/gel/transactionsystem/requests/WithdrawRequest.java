package org.gel.transactionsystem.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequest {

    private long id;
    private long amount;
    private long transactionId;

}
