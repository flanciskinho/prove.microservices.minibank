package org.example.minibank.accountoperation.security;

import java.util.ArrayList;
import java.util.List;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final List<String> authorities;

    static {
        authorities = new ArrayList();
        authorities.add(ADMIN);
        authorities.add(USER);
        authorities.add(ANONYMOUS);
    }

    private AuthoritiesConstants() {
    }
}
