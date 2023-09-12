
package com.vis.monitor.scheduler;

import com.vis.monitor.modal.MonitorRequest;
import com.vis.monitor.modal.Server;
import com.vis.monitor.modal.User;
import com.vis.monitor.service.EmailService;
import com.vis.monitor.service.MonitorRequestService;
import com.vis.monitor.service.MonitorStatusService;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import com.vis.monitor.modal.MonitorStatus;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.HashMap;

@Component
@Log4j2
@Transactional
public class ScheduleTask {

	@Autowired
	private MonitorRequestService mrService;
	
	@Autowired
	private MonitorStatusService msService;
	
	@Autowired
	private EmailService emailService;
	
	private Map<Server, Boolean> prvState = new HashMap<>();

//	@Scheduled(fixedDelay = 1000)
	public void checkIpPortsAndSendEmails() {
		
		log.info("Scheduled task to monitor IP and Port started.");

		List<MonitorRequest> monitorRequests = mrService.getMonitorRequests();
		
		for (MonitorRequest monitorRequest : monitorRequests) {
			
			Server server = monitorRequest.getServer();
		
			boolean isReachable = checkIpAddressAndPort(server);
			
			boolean wasReachable = prvState.getOrDefault(monitorRequest, true);

			if (!isReachable && wasReachable) {
				log.warn("IP {} and Port {} are not reachable.", server.getHost(), server.getPort());
				
				List<User> users = monitorRequest.getUsers(); 

				if (users != null) {
					
					for(User user : users) {
						sendPortNotReachableEmail(user, server);
					}
					
				} else {
					log.warn("No user details found for IP {} and Port {}.", server.getHost(), server.getPort());
				}

			} else if (isReachable && !wasReachable) {
				
				log.info("IP {} and Port {} are reachable again.", server.getHost(), server.getPort());

				List<User> users = monitorRequest.getUsers();
				
				if (users != null) {
					for(User user : users) {
						
						sendPortReachableEmail(user, server);
						
					}
				} else {					
					log.warn("No user details found for IP {} and Port {}.", server.getHost(), server.getPort());					
				}
			}

			prvState.put(monitorRequest.getServer(), isReachable);
		}

		log.info("Scheduled task to monitor IP and Port completed.");
	}
	

	// checking ip and port method
	private boolean checkIpAddressAndPort(Server server) {
		return checkIpAddressAndPort(server.getHost(), server.getPort());
	}

	// checking ip and port method
	private boolean checkIpAddressAndPort(String ipAddress, int portNumber) {
		
		try (Socket socket = new Socket()) {
			
			int timeout = 1000;
			
			socket.connect(new InetSocketAddress(ipAddress, portNumber), timeout);
			
			log.debug("Successfully connected to IP address {} and port number {}.", ipAddress, portNumber);
			
			return true;
			
		} catch (IOException e) {
			
			log.debug("Failed to connect to IP address {} and port number {}.", ipAddress, portNumber);
			
			return false;
		}
	}

	private void sendPortNotReachableEmail(User user, Server server) {

		List<MonitorStatus> monitorStatuses = msService.getMonitorStatusesByUser(user);

		boolean isSent = false;

		for (MonitorStatus monitorStatus : monitorStatuses) {
			if (monitorStatus.getIsSent()) {
				isSent = true;
				log.info("Email already sent to {} for unreachable IP and port.", user);
				break;
			}
		}

		if (!isSent) {
			
			MonitorStatus monitorStatus = null;
			
			try {
				
				String subject = "Port Not Reachable Alert";
				String message = "The IP address " + server.getHost() + " and port " + server.getPort() + " are not reachable.";

				isSent = emailService.sendEmail(user.getEMail(), subject, message);

				monitorStatus = new MonitorStatus();
				monitorStatus.setUser(user);
				monitorStatus.setIsSent(isSent);
				monitorStatus.setSentAt(new Date());

				log.info("Email notification sent to {} successfully.", user);
				
			} catch (Exception ex) {
				
				log.error("Failed to send email notification to {} for unreachable IP and port: {}", user, ex.getMessage());
 
				monitorStatus = new MonitorStatus();
				monitorStatus.setUser(user);
				monitorStatus.setIsSent(isSent);
				monitorStatus.setSentAt(new Date());

				
			}
			
			msService.addMonitorStatus(monitorStatus);
		}

	}

//  to notify again to user method 
	private void sendPortReachableEmail(User user, Server server) {

		List<MonitorStatus> monitorStatuses = msService.getMonitorStatusesByUser(user);

		boolean isSent = false;

		for (MonitorStatus monitorStatus : monitorStatuses) {
			if (monitorStatus.getIsSent()) {
				isSent = true;
				log.info("Email already sent to {} for unreachable IP and port.", user);
				break;
			}
		}

		if (!isSent) {
			try {
				MonitorStatus monitorStatus = new MonitorStatus();
				monitorStatus.setUser(user);
				monitorStatus.setIsSent(isSent);
				monitorStatus.setSentAt(new Date());

				msService.addMonitorStatus(monitorStatus);

				log.info("Email notification sent to {} successfully.", user);
			} catch (Exception ex) {
				log.error("Failed to send email notification to {} for unreachable IP and port: {}", user,
						ex.getMessage());

				MonitorStatus monitorStatus = new MonitorStatus();
				monitorStatus.setUser(user);
				monitorStatus.setIsSent(isSent);
				monitorStatus.setSentAt(new Date());

				msService.addMonitorStatus(monitorStatus);
			}
		}
	}
}
