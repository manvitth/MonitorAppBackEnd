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
            System.out.println("Email Service | Sending Mail | To : "+to);
            System.out.println("Email Service | Sending Mail | Subject : "+subject);
            System.out.println("Email Service | Sending Mail | Message : "+message);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderMail);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);

            isSent = true;
        }catch(Exception ex){
            System.out.println("Email Service | Exception : "+ex.getMessage());
            ex.printStackTrace();
        }
        return isSent;
    }



@Override
public Boolean sendEmailNotified(String to, String subject, String message) {
    Boolean isSent = false;
    try{
        System.out.println("Email Service | Sending Mail | To : "+to);
        System.out.println("Email Service | Sending Mail | Subject : "+subject);
        System.out.println("Email Service | Sending Mail | Message : "+message);

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