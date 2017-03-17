package org.example.minibank.accountoperation.repository;

import org.example.minibank.accountoperation.domain.AccountOperation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountOperation entity.
 */
@SuppressWarnings("unused")
public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {

}
