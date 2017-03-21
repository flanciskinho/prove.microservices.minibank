package org.example.minibank.account.service;

import org.example.minibank.account.config.FeignConfiguration;
import org.example.minibank.account.service.dto.OperationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Created by flanciskinho on 20/3/17.
 */
@FeignClient(
    name = "accountoperation",
    configuration = FeignConfiguration.class,
    fallback = AccountOperationFallback.class
)
public interface AccountOperationService {
    @RequestMapping(
        value = "api/account-operations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<OperationDTO> createOperation(
        @RequestBody OperationDTO operationDTO,
        @RequestHeader("Authorization") String bearerToken
    );
}

@Component
class AccountOperationFallback implements  AccountOperationService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseEntity<OperationDTO> createOperation(
        @RequestBody OperationDTO operationDTO,
        @RequestHeader("Authorization") String bearerToken
    ) {
        log.debug("Cannot create account operation {}", operationDTO);
        return ResponseEntity.badRequest().body(null);
    }

}
