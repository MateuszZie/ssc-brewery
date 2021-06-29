package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PasswordEncodingTest {

    static final String PASSWORD = "password";


    @Test
    void Ldap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();

        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));

        String result = ldap.encode(PASSWORD);

        assertTrue(ldap.matches(PASSWORD,result));
    }
    @Test
    void testNoOp(){
        PasswordEncoder noOP = NoOpPasswordEncoder.getInstance();

        System.out.println(noOP.encode(PASSWORD));
    }


    @Test
    void hashingExample(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salt = PASSWORD + "thisIsMySaltValue";
        System.out.println(DigestUtils.md5DigestAsHex(salt.getBytes()));


    }
}
