package org.thoughtworks.wayoflearning.facade.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.wayoflearning.mapper.DataSynchronizationMapper;
import org.thoughtworks.wayoflearning.model.dto.DataSynchronizationConfirmation;
import org.thoughtworks.wayoflearning.service.DataSynchronizationService;

@RestController
@RequiredArgsConstructor
public class DataSynchronizationController {

    private final DataSynchronizationService dataSynchronizationService;
    private final DataSynchronizationMapper dataSynchronizationMapper;

    @PostMapping("/school-contracts/{cid}/data/{id}/confirmation")
    public ResponseEntity<String> confirmation(@PathVariable("cid") long cid, @PathVariable("id") long id,
            DataSynchronizationConfirmation confirmation) {
        var request = dataSynchronizationMapper.toRequest(cid, id, confirmation);
        return ResponseEntity.ok(dataSynchronizationService.sendData(request));
    }
}
