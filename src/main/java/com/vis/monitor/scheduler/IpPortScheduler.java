
package com.vis.monitor.scheduler;


import com.vis.monitor.modal.IpPort;

import com.vis.monitor.modal.UserDetails; 
import com.vis.monitor.service.IpPortService;
import com.vis.monitor.service.UserDetailsService; 
import com.vis.monitor.service.impl.EmailServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.vis.monitor.modal.UserDetails;
import com.vis.monitor.service.IpPortService;
import com.vis.monitor.service.impl.EmailServiceImpl;
import com.vis.monitor.repository.EmailStatusRepository;
import com.vis.monitor.modal.EmailStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class IpPortScheduler {

    private static final Logger logger = LoggerFactory.getLogger(IpPortScheduler.class);

    private final IpPortService ipPortService;
    private final EmailServiceImpl emailServiceImpl;
    private final EmailStatusRepository emailStatusRepository;
    private final Map<IpPort,Boolean>previousReachabilityState=new HashMap<>();
    
 
    
    @Autowired
    public IpPortScheduler(IpPortService ipPortService, EmailServiceImpl emailServiceImpl, EmailStatusRepository emailStatusRepository) {
        this.ipPortService = ipPortService;
        this.emailServiceImpl = emailServiceImpl;
        this.emailStatusRepository=emailStatusRepository;
 
    }

    
//    scheuler method
    
    @Scheduled(fixedRate = 1000)
    public void checkIpPortsAndSendEmails() {
        logger.info("Scheduled task to monitor IP and Port started.");

        List<IpPort> ipPorts = ipPortService.getAllIpPorts();
        for (IpPort ipPort : ipPorts) {
            String ipAddress = ipPort.getIpAddress();
            int port = ipPort.getPort();

            boolean isReachable = checkIpAddressAndPort(ipAddress, port);
            boolean wasReachable = previousReachabilityState.getOrDefault(ipPort, true);



            if (!isReachable && wasReachable) {
                logger.warn("IP {} and Port {} are not reachable.", ipAddress, port);

                UserDetails userDetails = ipPort.getUserDetails();

                if (userDetails != null) {
                    String recipientEmail = userDetails.getEmail();

                    sendPortNotReachableEmail(recipientEmail, ipAddress, port);
                } else {
                    logger.warn("No user details found for IP {} and Port {}.", ipAddress, port);
                }

            } else if (isReachable && !wasReachable) {
                logger.info("IP {} and Port {} are reachable again.", ipAddress, port);
                UserDetails userDetails = ipPort.getUserDetails();
                if (userDetails != null) {
                    String recipientEmail = userDetails.getEmail();
                    sendPortReachableEmail(recipientEmail, ipAddress, port);
                } else {
                    logger.warn("No user details found for IP {} and Port {}.", ipAddress, port);
                }
            }

            previousReachabilityState.put(ipPort, isReachable);
        }

        logger.info("Scheduled task to monitor IP and Port completed.");
    }


    //checking ip and port method
    private boolean checkIpAddressAndPort(String ipAddress, int portNumber) {
        try (Socket socket = new Socket()) {
            int timeout = 1000;
            socket.connect(new InetSocketAddress(ipAddress, portNumber), timeout);
            logger.debug("Successfully connected to IP address {} and port number {}.", ipAddress, portNumber);
            return true;
        } catch (IOException e) {
            logger.debug("Failed to connect to IP address {} and port number {}.", ipAddress, portNumber);
            return false;
        }
    }


    
        


//sending email method
    private void sendPortNotReachableEmail(String emailAddress, String ipAddress, int portNumber) {
    	
        List<EmailStatus> existingStatusList = emailStatusRepository.findByRecipientEmail(emailAddress);

        boolean emailSent = false;

        for (EmailStatus existingStatus : existingStatusList) {
            if ("SENT".equals(existingStatus.getStatus())) {
                emailSent = true;
                logger.info("Email already sent to {} for unreachable IP and port.", emailAddress);
                break;
            }
        }

        if (!emailSent) {
            try {
                String subject = "Port Not Reachable Alert";
                String message = "The IP address " + ipAddress + " and port " + portNumber + " are not reachable.";

                emailServiceImpl.sendEmail(emailAddress, subject, message);

                // sent email 
                EmailStatus emailStatus = new EmailStatus();
                emailStatus.setRecipientEmail(emailAddress);
                emailStatus.setStatus("SENT");
                emailStatus.setTimestamp(new Date());

                emailStatusRepository.save(emailStatus);

                logger.info("Email notification sent to {} successfully.", emailAddress);
            } catch (Exception ex) {
                logger.error("Failed to send email notification to {} for unreachable IP and port: {}", emailAddress, ex.getMessage());

//                failed email 
                EmailStatus emailStatus = new EmailStatus();
                emailStatus.setRecipientEmail(emailAddress);
                emailStatus.setStatus("FAILED");
                emailStatus.setTimestamp(new Date());

                emailStatusRepository.save(emailStatus);
            }
        }
        
    }

//    to notify again to user method 
        private void sendPortReachableEmail(String emailAddress, String ipAddress, int portNumber) {
            
            List<EmailStatus> existingStatusList = emailStatusRepository.findByRecipientEmail(emailAddress);

            boolean emailNotified = false;

            for (EmailStatus existingStatus : existingStatusList) {
                if ("SENT".equals(existingStatus.getStatus())) {
                    emailNotified = true;
                    logger.info("Email already sent to {} for unreachable IP and port.", emailAddress);
                    break;
                }
            }

            if (!emailNotified) {
                try {
                    EmailStatus emailStatus = new EmailStatus();
                    emailStatus.setRecipientEmail(emailAddress);
                    emailStatus.setStatus("NOTIFIED USER"); 
                    emailStatus.setTimestamp(new Date());

                    emailStatusRepository.save(emailStatus);

                    logger.info("Email notification sent to {} successfully.", emailAddress);
                } catch (Exception ex) {
                    logger.error("Failed to send email notification to {} for unreachable IP and port: {}", emailAddress, ex.getMessage());

                   
                    EmailStatus emailStatus = new EmailStatus();
                    emailStatus.setRecipientEmail(emailAddress);
                    emailStatus.setStatus("FAILED TO NOTIFY USER "); 
                    emailStatus.setTimestamp(new Date());

                    emailStatusRepository.save(emailStatus);
                }
            }
        }
        }

    






