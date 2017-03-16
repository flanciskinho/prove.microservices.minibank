package org.example.minibank.account.repository;

import org.example.minibank.account.domain.BankAccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {

}
