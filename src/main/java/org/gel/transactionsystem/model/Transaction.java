package org.gel.transactionsystem.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "transaction")
@ToString(of = {"transactionId"})
public class Transaction {

    @Id
    private Long transactionId;

    @ManyToOne
    private User user;

}
