package com.heang.drms_api.security;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration

public class BeanConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure a shared ObjectMapper that supports Java 8 date/time (LocalDateTime)
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // register JavaTimeModule to handle java.time types
        mapper.registerModule(new JavaTimeModule());
        // write dates as strings (not timestamps) to respect @JsonFormat
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // ignore unknown properties during deserialization (safe default)
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
