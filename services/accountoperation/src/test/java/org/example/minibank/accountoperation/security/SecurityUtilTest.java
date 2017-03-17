package org.example.minibank.accountoperation.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtilTest {
    public static final String [] USER_ROLE = {AuthoritiesConstants.USER};
    public static final String [] ADMIN_USER_ROLE = {AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN};


    public static void authenticate(Long loginId, String loginname, String [] authorities) {
        // Authenticate
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String str: authorities) {
            authorityList.add(new SimpleGrantedAuthority(str));
        }

        CustomUserSession customUserSession = new CustomUserSession(loginname, "", authorityList, loginId.toString());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserSession, "", authorityList);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void deauthenticate() {
        SecurityContextHolder.clearContext();
    }
}
