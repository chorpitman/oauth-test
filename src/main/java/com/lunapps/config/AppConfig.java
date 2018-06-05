package com.lunapps.config;

import com.lunapps.config.oauth.MethodSecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"com.lunapps"})
@Import({WebMvcConfig.class, MethodSecurityConfig.class})
@PropertySource(value = {"classpath:application.properties", "classpath:messages_en.properties",
        "classpath:templates/mail/msg/MailMessages.properties", "classpath:category.properties"})
public class AppConfig {
}