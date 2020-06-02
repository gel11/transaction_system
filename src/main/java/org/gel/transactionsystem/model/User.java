package org.gel.transactionsystem.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
@ToString(of = {"id", "balance", "name"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    private Long balance;

    @Getter
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Transaction> transactions = Collections.emptyList();
}
