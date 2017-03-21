package org.example.minibank.gateway.service.dto;

import org.example.minibank.gateway.config.Constants;
import org.example.minibank.gateway.domain.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by flanciskinho on 17/3/17.
 */
public class LoginProfileDTO {

    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(min = 2, max = 5)
    private String langKey;

    public LoginProfileDTO() {
    }

    public LoginProfileDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.langKey = user.getLangKey();
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    @Override
    public String toString() {
        return "LoginProfileDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", langKey='" + langKey + '\'' +
            '}';
    }
}
