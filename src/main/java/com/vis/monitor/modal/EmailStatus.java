package com.vis.monitor.modal;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "email_status")
public class EmailStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_email",unique=true)
    private String recipientEmail;

    @Column(name = "status")
    private String status; 

    @Column(name = "timestamp")
    private Date timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public EmailStatus(Long id, String recipientEmail, String status, Date timestamp) {
		super();
		this.id = id;
		this.recipientEmail = recipientEmail;
		this.status = status;
		this.timestamp = timestamp;
	}

	public EmailStatus() {
		super();
	}

	@Override
	public String toString() {
		return "EmailStatus [id=" + id + ", recipientEmail=" + recipientEmail + ", status=" + status + ", timestamp="
				+ timestamp + "]";
	}

  
}
