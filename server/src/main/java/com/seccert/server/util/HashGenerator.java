package com.seccert.server.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "Test@123";
        System.out.println(new BCryptPasswordEncoder().encode(raw));
    }
}
