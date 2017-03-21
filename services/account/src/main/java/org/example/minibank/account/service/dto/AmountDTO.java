package org.example.minibank.account.service.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by flanciskinho on 20/3/17.
 */
public class AmountDTO {

    @NotNull
    private Long accountId;

    @NotNull
    private BigDecimal amount;

    public AmountDTO() {
    }

    public AmountDTO(Long accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AmountDTO{" +
            "accountId=" + accountId +
            ", amount=" + amount +
            '}';
    }
}
