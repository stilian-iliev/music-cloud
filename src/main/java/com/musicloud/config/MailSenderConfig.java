package com.musicloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import java.util.Properties;

@Configuration
public class MailSenderConfig {
    @Bean
    public JavaMailSender javaMailSender(
            @Value("${mail.enabled:true}") Boolean mailEnabled,
            @Value("${mail.host}") String mailHost,
            @Value("${mail.port}") Integer mailPort,
            @Value("${mail.username}") String userName,
            @Value("${mail.password}") String password

    ) throws MessagingException {
        if (!mailEnabled) return null;

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(mailPort);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(mailProperties());

        return javaMailSender;
    }

    private Properties mailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.starttls.enable", "true");

//        properties.setProperty("mail.debug", "true");
        return properties;
    }
}
