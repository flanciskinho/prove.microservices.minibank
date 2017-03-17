package org.example.minibank.accountoperation.service.mapper;

import org.example.minibank.accountoperation.domain.*;
import org.example.minibank.accountoperation.service.dto.AccountOperationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AccountOperation and its DTO AccountOperationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountOperationMapper {

    AccountOperationDTO accountOperationToAccountOperationDTO(AccountOperation accountOperation);

    List<AccountOperationDTO> accountOperationsToAccountOperationDTOs(List<AccountOperation> accountOperations);

    AccountOperation accountOperationDTOToAccountOperation(AccountOperationDTO accountOperationDTO);

    List<AccountOperation> accountOperationDTOsToAccountOperations(List<AccountOperationDTO> accountOperationDTOs);
}
