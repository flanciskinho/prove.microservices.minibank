package org.example.minibank.account.service.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by flanciskinho on 20/3/17.
 */
public class OperationDTO {

    private Long id;

    @NotNull
    private Long accountId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private AccountOperationType type;

    public enum AccountOperationType {
        ADD,WITHDRAW
    }

    public OperationDTO() {
    }

    public OperationDTO(Long id, Long accountId, BigDecimal amount, AccountOperationType type) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AccountOperationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + id +
            ", accountId=" + accountId +
            ", amount=" + amount +
            ", type=" + type +
            '}';
    }
}
