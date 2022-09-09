package com.wilczewski.partsshopadmin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordTest {

    @Test
    public void testEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String normalPassword = "wilczek";
        String encodedPassword = encoder.encode(normalPassword);
        System.out.println(encodedPassword);

        boolean match = encoder.matches(normalPassword, encodedPassword);

        assertThat(match).isTrue();
    }

}
