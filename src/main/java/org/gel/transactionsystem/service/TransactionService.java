package org.gel.transactionsystem.service;

import org.gel.transactionsystem.expcetion.NotEnoughBalanceException;
import org.gel.transactionsystem.expcetion.TransactionAlreadyExistsException;
import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.repository.TransactionRepository;
import org.gel.transactionsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransactionService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(UserRepository userRepository, UserService userService, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public Transaction debit(Long userId, Long amount, Long transactionId) {
        boolean exists = transactionRepository.existsById(transactionId);

        if (exists)
            throw new TransactionAlreadyExistsException(transactionId);

        User user = userService.findUserOrThrow(userId);

        if (user.getBalance() < amount)
            throw new NotEnoughBalanceException(user.getId());

        Long newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);

        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .user(user)
                .build();

        // We dont have to save here, given we have cascade
        // but we want to trigger prePersist to generate the timestamp
        // before we return the transaction, the same applies for credit
        transaction = transactionRepository.save(transaction);
        user.addTransaction(transaction);
        userRepository.save(user);


        return transaction;
    }

    @Transactional(rollbackOn = Exception.class)
    public Transaction credit(Long userId, Long amount, Long transactionId) {
        boolean exists = transactionRepository.existsById(transactionId);

        if (exists)
            throw new TransactionAlreadyExistsException(transactionId);

        User user = userService.findUserOrThrow(userId);

        Long newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);

        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .user(user)
                .build();

        transaction = transactionRepository.save(transaction);
        user.addTransaction(transaction);
        userRepository.save(user);

        return transaction;
    }

    public List<Transaction> getAllTransactionForUser(Long userId) {
        User user = userService.findUserOrThrow(userId);
        return user.getTransactions();
    }
}
