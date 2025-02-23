package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.security.BCrypt15PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void testBCrypt15() {

        PasswordEncoder bCrypt = new BCrypt15PasswordEncoder();

        System.out.println(bCrypt.encode("tiger"));

    }

        @Test
    void testBCrypt() {

        PasswordEncoder bCrypt = new BCryptPasswordEncoder();

        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode("mateusz"));
    }

    @Test
    void testSHA256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();

        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));
    }

    @Test
    void Ldap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();

        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("tiger"));

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
