	package com.vis.monitor.repository;
	
	
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	import com.vis.monitor.modal.EmailStatus;
	import java.util.List;
	@Repository
	public interface EmailStatusRepository extends JpaRepository<EmailStatus, Long> {
		 List<EmailStatus> findByRecipientEmail(String recipientEmail);
	}
	
