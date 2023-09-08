package com.vis.monitor.ws.modal;

import java.util.List;

@lombok.Data
public class WebServiceResponse {

	private String responseString;
	private Integer responseCode;	
	private List<Data> responseData;
	
}
