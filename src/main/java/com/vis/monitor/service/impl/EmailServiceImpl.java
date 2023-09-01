package com.vis.monitor.service.impl;

import com.vis.monitor.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String senderMail;

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public Boolean sendEmail(String to, String subject, String message) {
        Boolean isSent = false;
        try{

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderMail);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);

        }catch(Exception ex){
            System.out.println("Email Service | Exception : "+ex.getMessage());
            ex.printStackTrace();
        }
        return isSent;
    }
}
