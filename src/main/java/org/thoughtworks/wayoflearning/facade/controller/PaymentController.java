package org.thoughtworks.wayoflearning.facade.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.wayoflearning.mapper.PaymentMapper;
import org.thoughtworks.wayoflearning.model.dto.PaymentConfirmation;
import org.thoughtworks.wayoflearning.service.PaymentService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/school-contracts/{cid}/payments/{id}/confirmation")
    public ResponseEntity<String> confirmation(@PathVariable("cid") long cid, @PathVariable("id") long id,
            PaymentConfirmation paymentConfirmation) {
        var paymentRequest = paymentMapper.toRequest(cid, id, paymentConfirmation);
        return ResponseEntity.ok(paymentService.pay(paymentRequest));
    }
}
