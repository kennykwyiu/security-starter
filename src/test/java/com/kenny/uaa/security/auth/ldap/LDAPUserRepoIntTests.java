package com.kenny.uaa.security.auth.ldap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.ldap.DataLdapTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataLdapTest
@ActiveProfiles("test")
public class LDAPUserRepoIntTests {
    @Autowired
    private LDAPUserRepo userRepo;

    @Test
    public void giveUsernameAndPassword_ThenFindUserSuccess() {
        String username = "kenny";
        String password = "123456";
        Optional<LDAPUser> oUser = userRepo.findByUsernameAndPassword(username, password);
        assertTrue(oUser.isPresent());
    }

    @Test
    public void givenUsernameAndWrongPassword_ThenFindUserFail() {
        String username = "kenny";
        Optional<LDAPUser> oUser = userRepo.findByUsernameAndPassword(username, "bad_password");
        assertTrue(oUser.isEmpty());
    }
}
