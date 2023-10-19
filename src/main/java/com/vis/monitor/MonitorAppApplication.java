package com.vis.monitor;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vis.monitor.modal.User;
import com.vis.monitor.modal.enums.UserType;
import com.vis.monitor.service.UserService;

@SpringBootApplication
@EnableScheduling
public class MonitorAppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MonitorAppApplication.class, args);
	}

	@Autowired
	private UserService uService;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		try {

			User user = uService.getUser(1L);

			if (user == null) {
				user = new User();
				user.setId(1L);
				user.setName("su-admin");
				user.setPassword("admin@su");
				user.setType(UserType.SUPERUSER);
				user.setEMail("su-admin@visnet.in");
				user.setPhoneNumber("9999999999");

				uService.addUser(user);
				
			}
			
			String property = System.getProperty("java.library.path");
			String path = this.getClass().getClassLoader().getResource(".").getPath();
			path = path + "lib";
			System.out.println("Path : "+path);
			System.setProperty("java.library.path", path + ";" + property);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@PostConstruct
	public void init() {
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
	}

}
