package org.gel.transactionsystem.controller;

import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.requests.CreditRequest;
import org.gel.transactionsystem.requests.DebitRequest;
import org.gel.transactionsystem.service.TransactionService;
import org.gel.transactionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final UserService userService;

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/balances", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllBalances() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/balances/{userId}", method = RequestMethod.GET)
    public User getBalance(@PathVariable Long userId) {
        return userService.findUserOrThrow(userId);
    }

    @RequestMapping(value = "/debit", method = RequestMethod.POST)
    public Transaction debit(@RequestBody DebitRequest request) {
        return transactionService.debit(request.getId(), request.getAmount(), request.getTransactionId());
    }

    @RequestMapping(value = "/credit", method = RequestMethod.POST)
    public Transaction debit(@RequestBody CreditRequest request) {
        return transactionService.credit(request.getId(), request.getAmount(), request.getTransactionId());
    }

    @RequestMapping(value = "/history/{userId}", method = RequestMethod.GET)
    public List<Transaction> getAllTransactions(@PathVariable Long userId) {
        return transactionService.getAllTransactionForUser(userId);
    }


}
