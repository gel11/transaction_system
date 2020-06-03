package org.gel.transactionsystem.service;

import org.gel.transactionsystem.expcetion.NotEnoughBalanceException;
import org.gel.transactionsystem.expcetion.TransactionAlreadyExistsException;
import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.repository.TransactionRepository;
import org.gel.transactionsystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void debitWithInvalidTransactionId() {
        final Transaction transaction = Transaction.builder().transactionId(1L).build();

        given(transactionRepository.existsById(transaction.getTransactionId()))
                .willReturn(true);

        Assertions.assertThrows(TransactionAlreadyExistsException.class, () -> transactionService.debit(1L, 200L, 1L));

        verify(userRepository, never())
                .save(any(User.class));

        verify(transactionRepository, never())
                .save(any(Transaction.class));

    }

    @Test
    void creditWithInvalidTransactionId() {
        final Transaction transaction = Transaction.builder().transactionId(1L).build();

        given(transactionRepository.existsById(transaction.getTransactionId()))
                .willReturn(true);

        Assertions.assertThrows(TransactionAlreadyExistsException.class, () -> transactionService.debit(1L, 200L, 1L));

        verify(userRepository, never())
                .save(any(User.class));

        verify(transactionRepository, never())
                .save(any(Transaction.class));

    }

    @Test
    void debitWithNotEnoughBalance() {
        final Transaction transaction = Transaction.builder().transactionId(1L).build();
        final User user = User.builder().id(1L).balance(200L).build();

        given(transactionRepository.existsById(transaction.getTransactionId()))
                .willReturn(false);

        given(userService.findUserOrThrow(user.getId()))
                .willReturn(user);

        Assertions.assertThrows(NotEnoughBalanceException.class, () -> transactionService.debit(1L, 201L, 1L));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void debit() {
        final Transaction transaction = Transaction.builder().transactionId(1L).build();
        final User user = User.builder().id(1L).balance(201L).build();

        given(transactionRepository.existsById(transaction.getTransactionId()))
                .willReturn(false);

        given(userService.findUserOrThrow(user.getId()))
                .willReturn(user);

        given(userRepository.save(user))
                .willReturn(user);

        given(transactionRepository.save(any(Transaction.class)))
                .willAnswer(AdditionalAnswers.returnsFirstArg());

        transactionService.debit(1L, 201L, 1L);

        Assertions.assertEquals(0, user.getBalance());

        verify(userRepository, times(1))
                .save(user);
    }

    @Test
    void credit() {
        final Transaction transaction = Transaction.builder().transactionId(1L).build();
        final User user = User.builder().id(1L).balance(201L).build();

        given(transactionRepository.existsById(transaction.getTransactionId()))
                .willReturn(false);

        given(userService.findUserOrThrow(user.getId()))
                .willReturn(user);

        given(userRepository.save(user))
                .willReturn(user);

        given(transactionRepository.save(any(Transaction.class)))
                .willAnswer(AdditionalAnswers.returnsFirstArg());

        transactionService.credit(1L, 201L, 1L);

        Assertions.assertEquals(402L, user.getBalance());

        verify(userRepository, times(1))
                .save(user);
    }
}