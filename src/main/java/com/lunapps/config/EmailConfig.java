package com.lunapps.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by olegchorpita on 8/31/17.
 */
@Configuration
public class EmailConfig implements ApplicationContextAware, EnvironmentAware {
    private ApplicationContext applicationContext;
    private Environment environment;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Value("${mail.server.host}")
    private String host;
    @Value("${mail.server.port}")
    private int port;
    @Value("${mail.server.protocol}")
    private String protocol;
    @Value("${mail.server.username}")
    private String username;
    @Value("${mail.server.password}")
    private String password;
    @Value("${email.from}")
    private String from;

    /*
     * SPRING + JAVAMAIL: JavaMailSender instance, configured via .properties files.
     */
    @Bean
    public JavaMailSender mailSender() throws IOException {

        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // JavaMail-specific mail sender configuration, based on properties
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(getProperties());
        return mailSender;
    }

    // Basic mail sender configuration, based on properties
    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.quitwait", "false");
        properties.put("mail.debug", "false");
        return properties;
    }
}
