package org.thoughtworks.wayoflearning.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.thoughtworks.wayoflearning.model.request.LearningManagementRequest;
import org.thoughtworks.wayoflearning.model.response.LearningManagementResponse;

@FeignClient(name = "lmsClient", url = "https://learning-management.com/data")
public interface LearningManagementSystemClient {

    ResponseEntity<LearningManagementResponse> sendData(LearningManagementRequest request);
}
