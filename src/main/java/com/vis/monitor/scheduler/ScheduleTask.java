package com.vis.monitor.scheduler;

import com.vis.monitor.modal.IpPort;
import com.vis.monitor.modal.UserDetails; // Import UserDetails
import com.vis.monitor.service.IpPortService;
import com.vis.monitor.service.UserDetailsService; // Import UserDetailsService
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final UserDetailsService userDetailsService; // Inject UserDetailsService
    private final JavaMailSender javaMailSender;

    public IpPortScheduler(IpPortService ipPortService, UserDetailsService userDetailsService, JavaMailSender javaMailSender) {
        this.ipPortService = ipPortService;
        this.userDetailsService = userDetailsService;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(fixedRate = 1000)
    public void checkIpPorts() {
        logger.info("Scheduled task to monitor IP and Port started.");

        List<IpPort> ipPorts = ipPortService.getAllIpPorts();
        for (IpPort ipPort : ipPorts) {
            String ipAddress = ipPort.getIpAddress();
            int port = ipPort.getPort();

            boolean isReachable = checkIpAddressAndPort(ipAddress, port);

            if (isReachable) {
                logger.info("IP {} and Port {} are reachable.", ipAddress, port);
            } else {
                logger.warn("IP {} and Port {} are not reachable.", ipAddress, port);
              
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

}


