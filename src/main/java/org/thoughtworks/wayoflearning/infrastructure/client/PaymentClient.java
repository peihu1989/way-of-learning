package org.thoughtworks.wayoflearning.infrastructure.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.thoughtworks.wayoflearning.model.request.PaymentRequest;
import org.thoughtworks.wayoflearning.model.response.PaymentResponse;

@FeignClient(name = "paymentClient", url = "https://payment.com/payment")
public interface PaymentClient {

    ResponseEntity<PaymentResponse>  pay(PaymentRequest paymentRequest);

}
