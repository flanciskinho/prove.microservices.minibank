package org.example.minibank.accountoperation.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import org.example.minibank.accountoperation.domain.enumeration.AccountOperationType;

/**
 * A DTO for the AccountOperation entity.
 */
public class AccountOperationDTO implements Serializable {

    private Long id;

    @NotNull
    private Long accountId;

    private ZonedDateTime date;

    @NotNull
    private AccountOperationType type;

    @NotNull
    private BigDecimal amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    public AccountOperationType getType() {
        return type;
    }

    public void setType(AccountOperationType type) {
        this.type = type;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountOperationDTO accountOperationDTO = (AccountOperationDTO) o;

        if ( ! Objects.equals(id, accountOperationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountOperationDTO{" +
            "id=" + id +
            ", accountId='" + accountId + "'" +
            ", date='" + date + "'" +
            ", type='" + type + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
