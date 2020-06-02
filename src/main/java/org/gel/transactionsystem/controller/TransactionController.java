package org.gel.transactionsystem.controller;

import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.requests.WithdrawRequest;
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

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public Transaction withdraw(@RequestBody WithdrawRequest request) {
        return transactionService.withdraw(request.getId(), request.getAmount(), request.getTransactionId());
    }


}
