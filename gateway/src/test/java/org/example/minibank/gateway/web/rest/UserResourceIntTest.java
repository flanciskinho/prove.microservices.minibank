package org.example.minibank.gateway.web.rest;

import org.apache.log4j.spi.LoggerFactory;
import org.example.minibank.gateway.GatewayApp;
import org.example.minibank.gateway.domain.User;
import org.example.minibank.gateway.repository.UserRepository;
import org.example.minibank.gateway.service.UserService;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.minibank.gateway.service.dto.LoginProfileDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApp.class)
public class UserResourceIntTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    private MockMvc restUserMockMvc;

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setLogin("test");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("test@test.com");
        user.setFirstName("test");
        user.setLastName("test");
        user.setLangKey("en");
        em.persist(user);
        em.flush();
        return user;
    }

    @Before
    public void setup() {
        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(userResource, "userService", userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.lastName").value("Administrator"));
    }

    @Test
    public void testGetExistingLogin() throws Exception {
        List<User> users = new ArrayList(2);
        users.add(userService.getUserWithAuthoritiesByLogin("admin").get());
        users.add(userService.getUserWithAuthoritiesByLogin("user" ).get());

        List<Long> ids = users.stream().map(u -> u.getId()).collect(Collectors.toList());
        List<LoginProfileDTO> profiles = users.stream().map(LoginProfileDTO::new).collect(Collectors.toList());

        String pathVariable = "";
        for (Long tmp: ids)
            pathVariable = pathVariable + "," + tmp;
        pathVariable = pathVariable.substring(1);

        ResultActions resultActions = restUserMockMvc.perform(get("/api/users/login/"+pathVariable)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        for (LoginProfileDTO p: profiles)
            resultActions
                .andExpect(jsonPath("$.[*].id").value(hasItem(p.getId().intValue())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(p.getLogin())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(p.getFirstName())))
                .andExpect(jsonPath("$.[*].langKey").value(hasItem(p.getLangKey())));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
