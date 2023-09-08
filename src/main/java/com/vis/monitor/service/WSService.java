package com.vis.monitor.service;

import java.util.List;

import com.vis.monitor.ws.modal.WebService;
import com.vis.monitor.ws.modal.WebServiceRequest;
import com.vis.monitor.ws.modal.WebServiceResponse;

public interface WSService {

	public WebService addWebService(WebService webService);
	
	public WebService updateWebService(WebService webService);
	
	public List<WebService> getWebServices();
	
	public WebService getWebService(Long id);
	
	public WebService getWebService(String name);
	
	public WebService deleteWebService(Long id);

	public WebServiceResponse executeWebService(WebServiceRequest request);
	
}
