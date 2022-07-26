package com.musicloud.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    void sendEmail(String to, String subject, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("musicloud@fastmail.com");
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
