package com.musicloud.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${mail.username}")
    private String from;
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);

        msg.setTo(to);

        msg.setSubject(subject);
        msg.setText(message);


        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
