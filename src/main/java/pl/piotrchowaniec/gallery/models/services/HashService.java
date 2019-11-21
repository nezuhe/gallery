package pl.piotrchowaniec.gallery.models.services;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    public String hashString(String stringToEncode) {
        return getBCrypt().encode(stringToEncode);
    }

    boolean isTypedStringCorrect(String typed, String hashed) {
        return getBCrypt().matches(typed, hashed);
    }

    @Bean
    public BCryptPasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder();
    }
}
