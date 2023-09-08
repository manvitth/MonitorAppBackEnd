package com.vis.monitor.service;

public interface EmailService {

    public Boolean sendEmail(String to, String subject, String message);

	public Boolean sendEmailNotified(String to, String subject, String message);
}
