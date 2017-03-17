package org.example.minibank.accountoperation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.example.minibank.accountoperation.domain.enumeration.AccountOperationType;

/**
 * A AccountOperation.
 */
@Entity
@Table(name = "account_operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AccountOperationType type;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
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

    public AccountOperation accountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public AccountOperation date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public AccountOperationType getType() {
        return type;
    }

    public AccountOperation type(AccountOperationType type) {
        this.type = type;
        return this;
    }

    public void setType(AccountOperationType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AccountOperation amount(BigDecimal amount) {
        this.amount = amount;
        return this;
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
        AccountOperation accountOperation = (AccountOperation) o;
        if(accountOperation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, accountOperation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountOperation{" +
            "id=" + id +
            ", accountId='" + accountId + "'" +
            ", date='" + date + "'" +
            ", type='" + type + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
