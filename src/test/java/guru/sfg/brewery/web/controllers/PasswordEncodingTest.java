package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;


public class PasswordEncodingTest {

    static final String PASSWORD = "password";


    @Test
    void hashingExample(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salt = PASSWORD + "thisIsMySaltValue";
        System.out.println(DigestUtils.md5DigestAsHex(salt.getBytes()));


    }
}
