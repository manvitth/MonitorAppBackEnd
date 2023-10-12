package com.vis.monitor.controller;

import com.vis.monitor.modal.Server;
import com.vis.monitor.modal.ServerHourlyStatus;
import com.vis.monitor.modal.ServerStatus;
import com.vis.monitor.service.ServerService;
import com.vis.monitor.service.ServerStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")

public class ServerController {

	@Autowired
	private ServerService sService;

	@Autowired
	private ServerStatusService ssService;

	@PostMapping("/add-server")
	public ResponseEntity<Server> addServer(@RequestBody Server server) {
		Server addedServer = sService.addServer(server);
		return ResponseEntity.ok(addedServer);
	}

	@PutMapping("/update-server")
	public ResponseEntity<Server> updateServer(@RequestBody Server server) {
		Server addedServer = sService.updateServer(server);
		return ResponseEntity.ok(addedServer);
	}

	@GetMapping("/get-servers")
	public ResponseEntity<List<Server>> getServers() {
		List<Server> servers = sService.getServers();
		return ResponseEntity.ok(servers);
	}

	@GetMapping("/get-server/{id}")
	public ResponseEntity<Server> getServerById(@PathVariable Long id) {
		Server server = sService.getServer(id);
		if (server != null) {
			return ResponseEntity.ok(server);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/delete-server/{id}")
	public ResponseEntity<Server> deleteIpPort(@PathVariable Long id) {
		Server deletedServer = sService.deleteServer(id);
		return ResponseEntity.ok(deletedServer);
	}

	@GetMapping("/get-server-status/{id}")
	public ResponseEntity<ServerStatus> getServerStatus(@PathVariable Long id) {
		Server server = sService.getServer(id);
		if (server != null) {
			ServerStatus sStatus = ssService.getTopServerStatusByServer(server);

			if (sStatus != null) {
				return ResponseEntity.ok(sStatus);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/get-server-hourly-status/{id}")
	public ResponseEntity<List<ServerHourlyStatus>> getServerHourlyStatusByServerId(@PathVariable Long id) {
		List<ServerHourlyStatus> hourlyStatus = new ArrayList<ServerHourlyStatus>();

		Map<String, ServerHourlyStatus> hmHourStatus = new LinkedHashMap<String, ServerHourlyStatus>();
		
		Server server = sService.getServer(id);

		if (server != null) {

			LocalDateTime now = LocalDateTime.now();

			String strDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));

			String strEndTime = strDate + ":59:59";

			Date endDate = new Date();

			try {
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strEndTime);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			now = now.minusDays(1);

			strDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));

			String strStartTime = strDate + ":00:00";

			Date startDate = new Date();

			try {
				startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartTime);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			List<ServerStatus> serverStatuses = ssService.getServerStatusByServerAndStartTimeAndEndTime(server,
					startDate, endDate);

			Date stDate = startDate;
			Date stDateBackup = startDate;
			
			String nowDateHour = new SimpleDateFormat("yyyy-MM-dd HH").format(new Date());
			String sDateHour = new SimpleDateFormat("yyyy-MM-dd HH").format(stDate);
			
			while (!nowDateHour.equals(sDateHour)) {
	
				Integer hour = null;
				Long duration = null;				
				
				for (ServerStatus serverStatus : serverStatuses) {

					Date downAt = serverStatus.getDownAt();

					Date upAt = serverStatus.getUpAt() != null ? serverStatus.getUpAt() : new Date();

					boolean isDownTimeFound = false;
					
					ServerHourlyStatus hourStatus = null;
					
					while (!nowDateHour.equals(sDateHour)) {
					
						hourStatus = null;
						
						Instant iNextHour = stDate.toInstant().plusSeconds(((59 * 60) + 59));

						Date nextHour = Date.from(iNextHour);

						boolean das = downAt.after(stDate);
						boolean dbs = downAt.before(stDate);

						boolean dbn = downAt.before(nextHour);

						boolean des = downAt.equals(stDate);
						boolean den = downAt.equals(nextHour);
						
						boolean uas = upAt.after(stDate);

						boolean ubn = upAt.before(nextHour);
						
						boolean uen = upAt.equals(nextHour);

						sDateHour = new SimpleDateFormat("yyyy-MM-dd HH").format(stDate);
						
						String sHour = new SimpleDateFormat("HH").format(stDate);
						
						hour = Integer.parseInt(sHour);
						
						duration = isDownTimeFound ? (nextHour.getTime() - stDate.getTime()) : 0L;
						
						if ((dbs || des) && (dbn || den)) {
							// found start of down time and end of up time
							duration = nextHour.getTime() - stDate.getTime(); 
							isDownTimeFound = true;
						}
						
						if ((das || des) && dbn) {
							// found start of down time and end of up time
							duration = nextHour.getTime() - downAt.getTime(); 
							isDownTimeFound = true;
						}

						if (uas && ( ubn || uen)) {
							// found end of down time and start of up time							
							if ((das || des) && dbn) {
								// found start of down time and end of up time
								duration = upAt.getTime() - downAt.getTime();
								isDownTimeFound = true;
							}else {
								duration = upAt.getTime() - stDate.getTime();
							}
							stDateBackup = stDate;
							break;
						}
						
						stDateBackup = stDate;
						
						Instant iStartDate = nextHour.toInstant().plusSeconds(1);

						stDate = Date.from(iStartDate);

						iNextHour = stDate.toInstant().plusSeconds(((59 * 60) + 59));

						nextHour = Date.from(iNextHour);

						long diffInMillies = Math.abs(duration);
					    long seconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
					    seconds = seconds == 0 ? 0 : seconds + 1;
					    
					    
					    if(hmHourStatus.containsKey(sDateHour)) {
					    	hourStatus = hmHourStatus.get(sDateHour); 
					    	seconds = hourStatus.getDuration() + seconds;
					    }
					    
					    if(hourStatus == null) {
					    	
					    	hourStatus = new ServerHourlyStatus(stDateBackup, stDateBackup, hour, seconds);
					    }
					    
					    hmHourStatus.put(sDateHour, hourStatus);
					}
					
					long diffInMillies = Math.abs(duration);
				    long seconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
				    seconds = seconds == 0 ? 0 : seconds + 1;
				    
				    if(hmHourStatus.containsKey(sDateHour)) {
				    	hourStatus = hmHourStatus.get(sDateHour); 
				    	seconds = hourStatus.getDuration() + seconds;
				    }
				    
				    if(hourStatus == null) {
				    	hourStatus = new ServerHourlyStatus(stDateBackup, stDateBackup, hour, seconds);
				    }
				    
				    hmHourStatus.put(sDateHour, hourStatus);
					
				}

			}
			
			hourlyStatus.addAll(hmHourStatus.values());
			
			return ResponseEntity.ok(hourlyStatus);

		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
