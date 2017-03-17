package org.example.minibank.account.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * A DTO for the BankAccount entity.
 */
public class BankAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal balance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankAccountDTO bankAccountDTO = (BankAccountDTO) o;

        if ( ! Objects.equals(id, bankAccountDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BankAccountDTO{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", balance='" + balance + "'" +
            '}';
    }
}
