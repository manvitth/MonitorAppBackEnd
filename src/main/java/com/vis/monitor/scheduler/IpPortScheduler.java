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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Component
public class IpPortScheduler {

    private static final Logger logger = LoggerFactory.getLogger(IpPortScheduler.class);

    private final IpPortService ipPortService;
    private final JavaMailSender javaMailSender;
    private final EmailServiceImpl emailServiceImpl;
    
    

    @Autowired
    public IpPortScheduler(IpPortService ipPortService, JavaMailSender javaMailSender,EmailServiceImpl emailServiceImpl) {
        this.ipPortService = ipPortService;
        this.javaMailSender = javaMailSender;
        this.emailServiceImpl=emailServiceImpl;
    }

    @Scheduled(fixedRate = 1000)
    public void checkIpPortsAndSendEmails() {
        logger.info("Scheduled task to monitor IP and Port started.");

        List<IpPort> ipPorts = ipPortService.getAllIpPorts();
        for (IpPort ipPort : ipPorts) {
            String ipAddress = ipPort.getIpAddress();
            int port = ipPort.getPort();

            boolean isReachable = checkIpAddressAndPort(ipAddress, port);

            if (!isReachable) {
                logger.warn("IP {} and Port {} are not reachable.", ipAddress, port);

                
                UserDetails userDetails = ipPort.getUserDetails();
                   
             
                if (userDetails != null) {
                    String recipientEmail = userDetails.getEmail();
                   
                    sendPortNotReachableEmail(recipientEmail, ipAddress, port);
                } else {
                    logger.warn("No user details found for IP {} and Port {}.", ipAddress, port);
                }
            } else {
                logger.info("IP {} and Port {} are reachable.", ipAddress, port);
            }
        }

        logger.info("Scheduled task to monitor IP and Port completed.");
    }

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

    private void sendPortNotReachableEmail(String emailAddress, String ipAddress, int portNumber) {
        try {
            String subject = "Port Not Reachable Alert";
            String message = "The IP address " + ipAddress + " and port " + portNumber + " are not reachable.";

            emailServiceImpl.sendEmail(emailAddress, subject, message);
            
            logger.info("Email notification sent to {} successfully ", emailAddress);
        } catch (Exception ex) {
            logger.error("Failed to send email notification", emailAddress, ex.getMessage());
        }
    }
    
    
    
}





