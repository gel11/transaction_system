package org.gel.transactionsystem.intergration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.gel.transactionsystem.expcetion.UserNotFoundException;
import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.repository.TransactionRepository;
import org.gel.transactionsystem.repository.UserRepository;
import org.gel.transactionsystem.requests.DebitRequest;
import org.gel.transactionsystem.requests.DefaultTransactionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DebitAndCreditIntegrationTest {

    private static final long TEST_USER_ID = 4;
    private static final long TEST_USER_START_BALANCE = 102;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Transactional
    public void testDebit() throws Exception {
        long debitAmount = 40L;
        User testUser = setup();
        DefaultTransactionRequest request = createRequest(debitAmount, 2L, testUser);

        Assertions.assertEquals(TEST_USER_START_BALANCE, testUser.getBalance());

        performPost("/debit", request)
                .andExpect(status().isOk());

        Assertions.assertEquals(TEST_USER_START_BALANCE - debitAmount, testUser.getBalance());

    }

    @Test
    @Transactional
    public void testCredit() throws Exception {
        long creditAmount = 40L;
        User testUser = setup();
        DefaultTransactionRequest request = createRequest(creditAmount, 2L, testUser);

        Assertions.assertEquals(TEST_USER_START_BALANCE, testUser.getBalance());

        performPost("/credit", request)
                .andExpect(status().isOk());

        Assertions.assertEquals(TEST_USER_START_BALANCE + creditAmount, testUser.getBalance());

    }

    @Test
    public void testDebitRollback() throws Exception {
        long debitAmount = 40L;
        User testUser = setup();
        DefaultTransactionRequest request = createRequest(debitAmount, 1L, testUser);

        Assertions.assertEquals(TEST_USER_START_BALANCE, testUser.getBalance());

        performPost("/debit", request)
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(TEST_USER_START_BALANCE, testUser.getBalance());
    }

    @Test
    public void testCreditRollback() throws Exception {
        long creditAmount = 40L;
        User testUser = setup();
        DefaultTransactionRequest request = createRequest(creditAmount, 1L, testUser);

        Assertions.assertEquals(TEST_USER_START_BALANCE, testUser.getBalance());

        performPost("/credit", request)
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(TEST_USER_START_BALANCE, testUser.getBalance());
    }

    private User setup() {
        User testUser = userRepository.findById(TEST_USER_ID)
                .orElseThrow(() -> new UserNotFoundException(TEST_USER_ID));

        Transaction transaction = Transaction.builder()
                .user(testUser)
                .transactionId(1L)
                .build();

        transactionRepository.save(transaction);
        return testUser;
    }

    private DefaultTransactionRequest createRequest(long amount,  long transactionId, User testUser) {
        DefaultTransactionRequest request = new DebitRequest();
        request.setAmount(amount);
        request.setTransactionId(transactionId);
        request.setId(testUser.getId());
        return request;
    }

    private ResultActions performPost(String path, DefaultTransactionRequest request) throws Exception {
        return mockMvc.perform(post(path)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }
}

