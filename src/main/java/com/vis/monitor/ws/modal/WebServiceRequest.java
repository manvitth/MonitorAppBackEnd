package com.vis.monitor.ws.modal;

import java.util.List;

@lombok.Data
public class WebServiceRequest {
	
	private String wsName;
	
	private List<Data> requestData;

}
