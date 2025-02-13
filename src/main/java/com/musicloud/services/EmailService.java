package com.musicloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class EmailService {
    @Value("${mail.username}")
    private String from;
    @Value("${server.host}")
    private String host;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(@Autowired(required = false) JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
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

    @Async
    public void sendResetPasswordEmail(String email, UUID id) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Reset password");

            Context ctx = new Context();
            ctx.setVariable("id", id);
            ctx.setVariable("host", host);
            mimeMessageHelper.setText(templateEngine.process("email/reset-password", ctx), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
