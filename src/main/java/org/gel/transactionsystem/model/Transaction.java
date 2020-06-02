package org.gel.transactionsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

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

    private Long createdTimestamp;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void prePersist() {
        createdTimestamp = Instant.now().getEpochSecond();
    }
}
