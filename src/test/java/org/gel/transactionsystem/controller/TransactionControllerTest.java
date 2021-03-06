package org.gel.transactionsystem.controller;

import org.gel.transactionsystem.advice.UserNotFoundAdvice;
import org.gel.transactionsystem.expcetion.UserNotFoundException;
import org.gel.transactionsystem.model.Transaction;
import org.gel.transactionsystem.model.User;
import org.gel.transactionsystem.service.TransactionService;
import org.gel.transactionsystem.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(userService, transactionService))
                .setControllerAdvice(new UserNotFoundAdvice())
                .build();
    }

    @Test
    public void testGetAllBalances() throws Exception {
        User user = getPredefinedUser();

        given(userService.getAllUsers())
                .willReturn(Collections.singletonList(user));

        mockMvc.perform(get("/balances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].name", is("Tester")))
                .andExpect(jsonPath("$[0].balance", is(54)))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetSpecificBalance() throws Exception {
        User user = getPredefinedUser();

        given(userService.findUserOrThrow(user.getId()))
                .willReturn(user);

        mockMvc.perform(get("/balances/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.balance", is(54)));


    }

    @Test
    public void testGetSpecificBalanceWithNonExistingId() throws Exception {
        User user = getPredefinedUser();

        given(userService.findUserOrThrow(user.getId()))
                .willThrow(new UserNotFoundException(user.getId()));

        String response = mockMvc.perform(get("/balances/{userId}", user.getId()))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(response, "Could not find user with id " + user.getId());
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        User user = getPredefinedUser();

        Transaction t1 = Transaction.builder()
                .user(user)
                .transactionId(1L)
                .build();

        Transaction t2 = Transaction.builder()
                .user(user)
                .transactionId(2L)
                .build();

        given(transactionService.getAllTransactionForUser(user.getId()))
                .willReturn(List.of(t1, t2));

        mockMvc.perform(get("/history/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].transactionId", is(1)))
                .andExpect(jsonPath("$[1].transactionId", is(2)))
                .andExpect(jsonPath("$", hasSize(2)));


    }

    private User getPredefinedUser() {
        return User.builder()
                .id(1L)
                .balance(54L)
                .name("Tester")
                .build();
    }
}