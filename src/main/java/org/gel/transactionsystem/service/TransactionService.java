package org.gel.transactionsystem.service;

import org.gel.transactionsystem.expcetion.NotEnoughBalanceException;
import org.gel.transactionsystem.expcetion.TransactionAlreadyExistsException;
import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.repository.TransactionRepository;
import org.gel.transactionsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Transaction withdraw(Long userId, Long amount, Long transactionId) {
        boolean exists = transactionRepository.existsById(transactionId);

        if (exists)
            throw new TransactionAlreadyExistsException(transactionId);

        User user = userService.findUserOrThrow(userId);

        if (user.getBalance() < amount)
            throw new NotEnoughBalanceException(user.getId());

        Long newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);

        user = userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionId(transactionId);

        transaction = transactionRepository.save(transaction);

        return transaction;
    }
}
