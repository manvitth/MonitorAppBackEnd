	
	package com.vis.monitor.scheduler;
	
	import com.vis.monitor.modal.MonitorRequest;
	import com.vis.monitor.modal.Server;
	import com.vis.monitor.modal.ServerStatus;
	import com.vis.monitor.modal.User;
	import com.vis.monitor.service.EmailService;
	import com.vis.monitor.service.MonitorRequestService;
	import com.vis.monitor.service.MonitorStatusService;
	import com.vis.monitor.service.ServerStatusService;
	
	import lombok.extern.log4j.Log4j2;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import com.vis.monitor.modal.MonitorStatus;
	
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Component;
	
	import java.io.IOException;
	import java.lang.management.ManagementFactory;
	import java.lang.management.OperatingSystemMXBean;
	import java.net.InetSocketAddress;
	import java.net.Socket;
	import java.util.List;
	import java.util.Map;
	import java.util.concurrent.TimeUnit;
	
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
		private ServerStatusService ssService;
		
		@Autowired
		private EmailService emailService;
		
		private Map<Server, Boolean> prvState = new HashMap<>();
	
		@Scheduled(fixedDelay = 10000)		
		public void checkIpPortsAndSendEmails() {
			
			log.info("Scheduled task to monitor IP and Port started.");
	
			List<MonitorRequest> monitorRequests = mrService.getMonitorRequests();
			
			for (MonitorRequest monitorRequest : monitorRequests) {
				
				Server server = monitorRequest.getServer();
			
				boolean isReachable = checkIpAddressAndPort(server);
				
				boolean wasReachable = true;
				
				if(prvState.containsKey(server)) { 
					wasReachable = prvState.get(server); 
				} else { 
					ServerStatus sStatus = ssService.getTopServerStatusByServer(server);
					wasReachable = sStatus.getUpAt() != null;
				}
	
				if (!isReachable && wasReachable) {
					log.warn("IP {} and Port {} are not reachable.", server.getHost(), server.getPort());
					
					ServerStatus sStatus = ssService.getServerStatusByServer(server);
					
					if(sStatus == null ) {
					
						sStatus = new ServerStatus();
						
						sStatus.setServer(server);
						sStatus.setDownAt(new Date());
					
						ssService.addServerStatus(sStatus);
					}
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
	
					ServerStatus sStatus = ssService.getServerStatusByServer(server);
					
					if(sStatus != null && sStatus.getUpAt() == null) {
					
						Date upAt = new Date();
						
						sStatus.setUpAt(upAt);
						
						long diffInMillies = Math.abs(upAt.getTime() - sStatus.getDownAt().getTime());
					    long duration = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
					    
					    sStatus.setDuration(duration);
					    
						ssService.updateServerStatus(sStatus);
					}
					List<User> users = monitorRequest.getUsers();
					
					if (users != null) {
						for(User user : users) {
							
							sendPortReachableEmail(user, server);
							
						}
					} else {					
						log.warn("No user details found for IP {} and Port {}.", server.getHost(), server.getPort());					
					}
				}
	
				prvState.put(server, isReachable);
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
		
		
		
		//checing cpu ram
	
		 @Scheduled(fixedDelay = 10000)
		    public void checkSystemMetrics() {
		        log.info("Scheduled task to monitor system metrics (RAM and CPU) started.");
	
		        // Check RAM and CPU usage
		        double cpuUsage = getCpuUsage();
		        long totalRAM = getTotalRAM();
		        long freeRAM = getFreeRAM();
		        
		        long totalMemory = getTotalMemory();
		        long usedMemory = getUsedMemory();
		        
		    
		        log.info("CPU Usage: {}%, Total RAM: {} bytes, Free RAM: {} bytes, Total Memory: {} bytes, Used Memory: {} bytes", 
		                cpuUsage, totalRAM, freeRAM, totalMemory, usedMemory);
	
		            log.info("Scheduled task to monitor system metrics (RAM and CPU) completed.");
		        }
	
	
		// Method to check CPU usage
		 private double getCpuUsage() {
		     OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		     if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
		         double load = ((com.sun.management.OperatingSystemMXBean) osBean).getSystemCpuLoad();
		         if (load >= 0) {
		             return load * 100.0;
		         }
		     }
		     // If the method is not available or the value is negative, return 0 (or another appropriate value).
		     return 0.0;
		 }
		    // Method to check total RAM
		    private long getTotalRAM() {
		        Runtime runtime = Runtime.getRuntime();
		        return runtime.totalMemory();
		    }
	
		    // Method to check free RAM
		    private long getFreeRAM() {
		        Runtime runtime = Runtime.getRuntime();
		        return runtime.freeMemory();
		    }
		    
		 // Method to check total memory
		    private long getTotalMemory() {
		        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
		            long totalMemory = ((com.sun.management.OperatingSystemMXBean) osBean).getTotalPhysicalMemorySize();
		            if (totalMemory >= 0) {
		                return totalMemory;
		            }
		        }
		        // If the method is not available or the value is negative, return 0 (or another appropriate value).
		        return 0;
		    }

		    // Method to check used memory
		    private long getUsedMemory() {
		        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
		            long totalPhysicalMemorySize = ((com.sun.management.OperatingSystemMXBean) osBean).getTotalPhysicalMemorySize();
		            long freePhysicalMemorySize = ((com.sun.management.OperatingSystemMXBean) osBean).getFreePhysicalMemorySize();
		            if (totalPhysicalMemorySize >= 0 && freePhysicalMemorySize >= 0) {
		                long usedMemory = totalPhysicalMemorySize - freePhysicalMemorySize;
		                if (usedMemory >= 0) {
		                    return usedMemory;
		                }
		            }
		        }
		        // If the method is not available or the values are negative, return 0 (or another appropriate value).
		        return 0;
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
					monitorStatus.setServer(server);
	
					
	
					log.info("Email notification sent to {} successfully.", user);
					
				} catch (Exception ex) {
					
					log.error("Failed to send email notification to {} for unreachable IP and port: {}", user, ex.getMessage());
	 
					monitorStatus = new MonitorStatus();
					monitorStatus.setUser(user);
					monitorStatus.setIsSent(isSent);
					monitorStatus.setSentAt(new Date());
					monitorStatus.setServer(server);
	
					
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
					monitorStatus.setServer(server);
	
	
					msService.addMonitorStatus(monitorStatus);
	
					log.info("Email notification sent to {} successfully.", user);
				} catch (Exception ex) {
					log.error("Failed to send email notification to {} for unreachable IP and port: {}", user,
							ex.getMessage());
	
					MonitorStatus monitorStatus = new MonitorStatus();
					monitorStatus.setUser(user);
					monitorStatus.setIsSent(isSent);
					monitorStatus.setSentAt(new Date());
					monitorStatus.setServer(server);
	
	
					msService.addMonitorStatus(monitorStatus);
				}
			}
		}
	}
