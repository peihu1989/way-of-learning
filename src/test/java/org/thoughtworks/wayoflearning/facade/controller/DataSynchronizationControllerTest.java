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
import org.thoughtworks.wayoflearning.enums.DataSynchronizationStatus;
import org.thoughtworks.wayoflearning.model.dto.DataSynchronizationConfirmation;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DataSynchronizationControllerTest extends WayOfLearningApplicationBaseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void should_get_synchronize_complete_when_call_data_synchronization_confirmation() {
        // given
        var confirmation = DataSynchronizationConfirmation.builder().build();
        when(dataSynchronizationService.sendData(argThat(argument -> argument.getContractId() == 1L && argument.getDataId() == 1L)))
                .thenReturn(DataSynchronizationStatus.COMPLETE.getMessage());

        // when & then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/school-contracts/1/data/1/confirmation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(confirmation)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(DataSynchronizationStatus.COMPLETE.getMessage()))
                    .andDo(MockMvcResultHandlers.print()
                );
    }

    @SneakyThrows
    @Test
    void should_get_synchronize_in_process_when_call_data_synchronization_confirmation() {
        // given
        var confirmation = DataSynchronizationConfirmation.builder().build();
        when(dataSynchronizationService.sendData(argThat(argument -> argument.getContractId() == 1L && argument.getDataId() == 2L)))
                .thenReturn(DataSynchronizationStatus.IN_PROCESS.getMessage());

        // when & then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/school-contracts/1/data/2/confirmation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(confirmation)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(DataSynchronizationStatus.IN_PROCESS.getMessage()))
                    .andDo(MockMvcResultHandlers.print()
                );
    }

    @SneakyThrows
    @Test
    void should_get_required_info_missing_when_call_data_synchronization_confirmation() {
        // given
        var confirmation = DataSynchronizationConfirmation.builder().build();
        when(dataSynchronizationService.sendData(argThat(argument -> argument.getContractId() == 1L && argument.getDataId() == 3L)))
                .thenReturn(DataSynchronizationStatus.REQUIRED_INFO_MISSING.getMessage());

        // when & then
        mockMvc.perform(
                    MockMvcRequestBuilders
                            .post("/school-contracts/1/data/3/confirmation")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(confirmation)))
                            .andExpect(status().isOk())
                            .andExpect(content().string(DataSynchronizationStatus.REQUIRED_INFO_MISSING.getMessage()))
                            .andDo(MockMvcResultHandlers.print()
                );
    }
}