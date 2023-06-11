package org.thoughtworks.wayoflearning.facade.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.thoughtworks.wayoflearning.WayOfLearningApplicationBaseTests;
import org.thoughtworks.wayoflearning.enums.PaymentStatus;
import org.thoughtworks.wayoflearning.model.dto.PaymentConfirmation;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PaymentControllerTest extends WayOfLearningApplicationBaseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void should_get_status_200_and_pay_success_when_call_payment_confirmation() {
        // given
        var confirmation = PaymentConfirmation.builder().amount(BigDecimal.ONE).build();
        when(paymentService.pay(argThat(s -> s.getContractId() == 1L && s.getPayId() == 1L)))
                .thenReturn(PaymentStatus.SUCCESS.getMessage());

        // when & then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/school-contracts/1/payments/1/confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmation)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(PaymentStatus.SUCCESS.getMessage()))
                        .andDo(MockMvcResultHandlers.print()
                );
    }

    @SneakyThrows
    @Test
    void should_get_status_200_and_insufficient_balance_when_call_payment_confirmation() {
        // given
        var confirmation = PaymentConfirmation.builder().amount(BigDecimal.ONE).build();
        when(paymentService.pay(argThat(s -> s.getContractId() == 1L && s.getPayId() == 2L)))
                .thenReturn(PaymentStatus.INSUFFICIENT_BALANCE.getMessage());

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/school-contracts/1/payments/2/confirmation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(confirmation)))
                .andExpect(status().isOk())
                .andExpect(content().string(PaymentStatus.INSUFFICIENT_BALANCE.getMessage()))
                .andDo(MockMvcResultHandlers.print()
                );
    }

    @SneakyThrows
    @Test
    void should_get_status_200_and_pay_failed_when_call_payment_confirmation() {
        // given
        var confirmation = PaymentConfirmation.builder().amount(BigDecimal.ONE).build();
        when(paymentService.pay(argThat(s -> s.getContractId() == 1L && s.getPayId() == 3L)))
                .thenReturn(PaymentStatus.FAILED.getMessage());

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/school-contracts/1/payments/3/confirmation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(confirmation)))
                .andExpect(status().isOk())
                .andExpect(content().string(PaymentStatus.FAILED.getMessage()))
                .andDo(MockMvcResultHandlers.print()
                );
    }

}