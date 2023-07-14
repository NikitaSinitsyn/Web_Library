package com.example.https.AppConfig;

import com.example.https.AppConfig.PositionSerializer;
import com.example.https.Entity.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean("mvcJacksonObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer mvcJacksonObjectMapperBuilderCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.addSerializer(Position.class, new PositionSerializer());
            builder.modules(module);
            // Дополнительные настройки builder
        };
    }

    @Bean
    public ObjectMapper objectMapper(@Qualifier("mvcJacksonObjectMapperBuilderCustomizer") Jackson2ObjectMapperBuilderCustomizer customizer) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        customizer.customize(builder);
        return builder.build();
    }
}